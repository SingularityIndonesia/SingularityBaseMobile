package plugin.postman_client_generator

import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.postman_client_generator.companion.addToSourceSet
import plugin.postman_client_generator.companion.find
import plugin.postman_client_generator.companion.removeNonAlphaNumeric
import java.io.File


class PostmanClientGenerator : Plugin<Project> {

    private val targetDir =
        "build/generated/kotlin/postman_client/"
    private val Project.namespace
        get() = group.toString().lowercase()
    private val json =
        Json { prettyPrint = true }

    override fun apply(
        target: Project
    ) {
        // adding targetDir to source set
        target.addToSourceSet(targetDir)

        // dump postman files
        val postmanFiles =
            target.projectDir.find(".postman_collection.json")

        // create clients
        val postmanClients = run {
            postmanFiles
                .map {
                    val groupName = run {
                        it.name
                            .replace(".postman_collection.json", "")
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
                println("Generating Postmant Client: ${client.nameSpace}${client.name}")

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
        val postman = json.decodeFromString<Postman>(file.readText())

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

                    val responseItem = item.response?.compareMerge()
                        ?: throw IllegalArgumentException("Please provide at least one response for:\n$item")

                    // check if request available
                    if (item.request != null)
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
                    else
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
                .flatten()
        }
    }

    // comparing response models to resolve null types
    private fun List<Postman.ResponseItem?>.compareMerge() : Postman.ResponseItem {
        // fixme: compare response parameter to resolve null type in value
        return first() ?: throw IllegalArgumentException("Please provide at least one response.")
    }

    private fun createClient(
        context: List<Context>,
        namespace: String,
        groupName: String,
        request: Postman.Request,
        response: Postman.ResponseItem
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