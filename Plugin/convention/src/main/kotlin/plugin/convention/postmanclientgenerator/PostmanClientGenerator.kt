package plugin.convention.postmanclientgenerator

import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.companion.addToSourceSet
import plugin.convention.companion.find
import plugin.convention.companion.removeNonAlphaNumeric
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
        setup(target)

        val postmanFiles =
            target.projectDir.find(".postman_collection.json")

        val result = postmanFiles
            .map {
                val clients = generateClients(it)
                val groupName = it.name
                    .replace(".postman_collection.json", "")
                    .split(".")
                    .joinToString("") { s ->
                        s.replaceFirstChar { c -> c.uppercase() }
                    }
                    .removeNonAlphaNumeric()

                val groupDir = File(target.projectDir, "$targetDir$groupName/")

                val namespace = "${target.namespace}.$groupName"

                clients
                    .map { client ->
                        // println("Generating Client -----------------------------------------------------------")
                        // println(json.encodeToString(client))
                        generateFile(
                            outputDir = groupDir,
                            namespace = namespace,
                            postmanClient = client
                        )
                    }
            }
            .flatten()
            .toList()

    }

    private fun generateFile(
        outputDir: File,
        namespace: String,
        postmanClient: PostmanClient
    ): File {
        val file = File(outputDir, "${postmanClient.name}.kt")
        file.parentFile.mkdirs()

        val content = "package $namespace\n\n${postmanClient.content}"
        file.writeText(content)

        return file
    }

    private fun setup(
        project: Project
    ) {
        project.addToSourceSet(targetDir)
    }

    private fun generateClients(
        file: File
    ): Sequence<PostmanClient> {
        val postman = json.decodeFromString<Postman>(file.readText())

        val items = postman.item?.asSequence()?.filterNotNull() ?: return sequenceOf()

        val requests = dumpRequest(
            contexts = listOf(),
            items = items
        )

        val clients = requests
            .map {
                toClient(
                    it.first,
                    it.second
                )
            }

        return clients
    }

    private fun dumpRequest(
        contexts: List<Context>,
        items: Sequence<Postman.ItemItem>
    ): Sequence<Pair<List<Context>, Postman.Request>> {

        return items
            .map {
                val newContexts =
                    contexts.plus(Context(it.name.toString()))

                if (it.request != null)
                    sequenceOf(
                        Pair(
                            newContexts,
                            it.request
                        )
                    )
                else
                    dumpRequest(
                        contexts = newContexts,
                        items = it.item?.asSequence()?.filterNotNull() ?: sequenceOf()
                    )
            }
            .flatten()
    }

    private fun toClient(
        context: List<Context>,
        request: Postman.Request
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
            request = request
        )
    }

}