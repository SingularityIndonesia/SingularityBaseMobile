/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.postman_client_generator.companion.ObjectType
import plugin.postman_client_generator.companion.addToSourceSet
import plugin.postman_client_generator.companion.compareMerge
import plugin.postman_client_generator.companion.find
import plugin.postman_client_generator.companion.jsonFormatter
import plugin.postman_client_generator.companion.removeNonAlphaNumeric
import plugin.postman_client_generator.companion.resolveJsonType
import java.io.File


class PostmanClientGenerator : Plugin<Project> {

    private val targetDir =
        "build/generated/kotlin/postman_client/"
    private val fileNameIdentifier = ".postman_collection.json"
    private val Project.namespace
        get() = group.toString().lowercase()
    private val json =
        Json { prettyPrint = true }

    override fun apply(
        target: Project
    ) {
        // adding targetDir to source set
        addToSourceSet(
            target,
            targetDir
        )

        // dump postman files
        val postmanFiles = find(
            target.projectDir,
            fileNameIdentifier
        )

        // create clients
        val postmanClients = run {
            postmanFiles
                .map {
                    val groupName = run {
                        it.name
                            .replace(fileNameIdentifier, "")
                            .split(".")
                            .joinToString("") { s ->
                                s.replaceFirstChar { c -> c.uppercase() }
                            }
                            .removeNonAlphaNumeric()
                    }
                    val namespace = "${target.namespace}.$groupName"
                    generateClients(
                        namespace,
                        groupName,
                        it
                    )
                }
                .flatten()
        }

        // generate files
        postmanClients
            .onEach { client ->
                /*println("Generating Postman Client: ${client.nameSpace}${client.name}")*/
                val outputDir = File(target.projectDir, "$targetDir${client.groupName}/")
                client.generateFile(
                    outputDir = outputDir
                )
            }
            .toList()
    }

    private fun generateClients(
        namespace: String,
        groupName: String,
        file: File
    ): Sequence<PostmanClient> {
        val postman = jsonFormatter.decodeFromString<Postman>(file.readText())

        val items = postman.item?.asSequence()?.filterNotNull() ?: return sequenceOf()

        val clients = dumpRequest(
            contexts = listOf(),
            namespace = namespace,
            groupName = groupName,
            items = items
        )

        return clients
    }

    private fun dumpRequest(
        contexts: List<Context>,
        namespace: String,
        groupName: String,
        items: Sequence<Postman.ItemItem>
    ): Sequence<PostmanClient> {

        /**
         * structures
         * item:
         *  -> request?
         *  -> item?
         *      -> request?
         *      -> item?
         *          ...
         *
         *  we are not flattening the request directly to keep the indentation.
         */
        return run {
            items
                .map { item ->
                    val newContexts =
                        contexts.plus(Context(item.name.toString()))

                    // check if request available
                    if (item.request != null) {

                        val responseItem = item.response
                            ?.filterNotNull()
                            ?.ifEmpty { throw IllegalArgumentException("Please provide at least one response for:\n$item") }
                            ?: throw IllegalArgumentException("Please provide at least one response for:\n$item")

                        sequenceOf(
                            createClient(
                                context = newContexts,
                                request = item.request,
                                namespace = namespace,
                                groupName = groupName,
                                response = responseItem
                            )
                        )
                        // if request not available then item is probably available
                    } else {
                        item.item?.asSequence()?.filterNotNull()
                            ?.let {
                                dumpRequest(
                                    contexts = newContexts,
                                    namespace = namespace,
                                    groupName = groupName,
                                    items = it
                                )
                            }
                            ?: sequenceOf()
                    }
                }
                .flatten()
        }
    }

    /**
     *  Comparing response bodies to resolve null types in body response
     */
    private fun List<Postman.ResponseItem?>.compareMerge(): Postman.ResponseItem {

        val newBody = run {
            val bodies = this
                .mapNotNull {
                    it?.body
                }
                .map {
                    jsonFormatter.parseToJsonElement(it)
                }
                .also {
                    if (it.isEmpty())
                        throw IllegalArgumentException("Please provide at least one response body.")

                    // fixme: support for type list
                    if (it.first() !is JsonObject)
                        throw IllegalArgumentException("Body is not a JSON object")

                }
                .map {
                    it.jsonObject
                }

            // fixme: support for type list
            val type = resolveJsonType(bodies)
                .let {
                    // fixme: support for type list
                    if (it != ObjectType)
                        throw IllegalArgumentException("Body is noe a JSON object")

                    it as ObjectType
                }

            // fixme: support for type list
            bodies
                .compareMerge()
                .toString()
        }

        return filterNotNull()
            .firstOrNull()
            ?.copy(body = newBody)
            ?: throw IllegalArgumentException("Please provide at least one response.")
    }

    private fun createClient(
        context: List<Context>,
        namespace: String,
        groupName: String,
        request: Postman.Request,
        response: List<Postman.ResponseItem>
    ): PostmanClient {
        val method = request.method ?: ""
        val name = method.plus(
            request.url?.path
                ?.asSequence()
                ?.filterNotNull()
                ?.filter {
                    !it.contains(":")
                }
                ?.filter {
                    !it.contains("{")
                }
                ?.map { path ->
                    path.replaceFirstChar { it.uppercase() }
                }
                ?.map {
                    val regex = Regex("""[^A-Za-z0-9]""")
                    regex.replace(it, "")
                }
                ?.fold("") { acc, v -> "$acc$v" }
                ?: throw UnknownError("Fail to decode ${request.url} to name.")
        )

        val strategy = when (method) {
            "GET", "POST", "PUT", "PATCH", "DELETE" ->
                CommonClientGenerator

            "HEAD" -> HEADClientGenerator
            "OPTIONS" -> OPTIONSClientGenerator
            else -> throw IllegalArgumentException("Unknown method: $method")
        }

        return strategy.generateClient(
            contexts = context,
            name = name,
            nameSpace = namespace,
            groupName = groupName,
            request = request,
            response = response
        )
    }

}