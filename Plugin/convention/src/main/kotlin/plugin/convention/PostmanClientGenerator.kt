package plugin.convention

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.companion.addToSourceSet
import plugin.convention.companion.find
import plugin.convention.postmanclientgenerator.Postman
import java.io.File
import java.util.regex.Pattern

@Serializable
private data class PostmanClient(
    val name: String,
    val content: String
)

@JvmInline
private value class Context(
    val value: String
)


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
            target.projectDir.find("postman_collection.json")

        val result = postmanFiles
            .map(::generateClients)
            .flatten()
            .onEach {
                println("Generating Client -----------------------------------------------------------")
                println(json.encodeToString(it))
                generateFile(
                    outputDir = File(target.projectDir, targetDir),
                    namespace = target.namespace,
                    postmanClient = it
                )
            }
            .toList()

    }

    private fun generateFile(
        outputDir: File,
        namespace: String,
        postmanClient: PostmanClient
    ) {
        val file = File(outputDir, "${postmanClient.name}.kt")
        file.parentFile.mkdir()

        val content = "package $namespace\n\n${postmanClient.content}"
        file.writeText(content)
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
                ?.fold("") { acc, v -> "$acc$v" }
                ?: throw UnknownError("Fail to decode ${request.url} to name.")
        )

        if (name.contains("{"))
            throw error(request)

        val strategy = when (method) {
            "GET","POST", "PUT", "PATCH", "DELETE" ->
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


private sealed interface ClientGeneratorStrategy {
    fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request
    ): PostmanClient
}

private object CommonClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request
    ): PostmanClient {
        val url = request.url

        /**
         * ```
         * "path": [
         * 	"books",
         * 	":bookID",
         * 	"comments"
         * ]
         * Into : books/${bookID}/comments
         */
        val path = url?.path
            ?.filterNotNull()
            ?.map {
                if (it.contains(":")) {
                    val pureName = it.replaceFirstChar { "" }
                    "\${$pureName}"
                } else {
                    it
                }
            }
            ?.fold("") { acc, v -> "$acc/$v" }
            ?: throw IllegalArgumentException("path is null:\n $request")

        val method = request.method
            ?: throw NullPointerException("method is null:\n$request")

        val query = url.query ?: listOf()
        val requestModelName = "${name}Request"
        val requestModel =
            RequestModel(
                name = requestModelName,
                queries = query.filterNotNull()
            )

        val responseModelName = "${name}Response"

        val function = generateFunction(
            functionName = name,
            method = method,
            requestModelName = requestModelName,
            responseModelName = responseModelName,
            endpoint = path
        )

        val final = "${requestModel.print()}\n\n$function"

        return PostmanClient(
            name = name,
            content = final
        )
    }
}

@JvmInline
private value class TypeToken(val value: String)

private data class RequestModel(
    val name: String,
    val queries: List<Postman.QueryItem>
) {
    companion object {
        fun resolveType(
            clue: String
        ): TypeToken {
            // fixme: for now everything is string
            return TypeToken("String")
        }
    }

    fun print(): String {

        val pt1 = """
            import io.ktor.client.HttpClient
            import io.ktor.client.request.*
            import io.ktor.client.statement.bodyAsText
            import kotlinx.coroutines.Dispatchers
            import kotlinx.coroutines.IO
            import kotlinx.serialization.Serializable
            import kotlinx.coroutines.withContext
            import kotlinx.serialization.json.Json
            import kotlinx.serialization.json.encodeToJsonElement
            import kotlinx.serialization.json.jsonObject
            
            @Serializable
            data class $name(
        """.trimIndent()

        val pt2 =
            queries
                .map {
                    "\tval ${it.key}: ${resolveType(it.value!!).value}?"
                }
                .fold("") { acc, v -> "$acc\n$v," }

        val pt3 = ")"

        return "$pt1\n$pt2\n$pt3"
    }
}

private fun generateFunction(
    functionName: String,
    method: String,
    requestModelName: String,
    responseModelName: String,
    endpoint: String
): String {

    val pathArguments = run {
        val pattern = Pattern.compile("""\{(.*?)\}""")
        val matcher = pattern.matcher(endpoint)
        val results = mutableListOf<String>()

        while (matcher.find()) {
            results.add(matcher.group(1))
        }

        results
    }

    val pt1 = """
        suspend fun $functionName(
            httpClient: HttpClient,
    """.trimIndent()

    val pt2 = pathArguments
        .joinToString(",\n") {
            "\t$it: String"
        }
        .let {
            if (it.isNotEmpty())
                it.plus(",")
            else
                it
        }

    val pt3 = """
            request: $requestModelName
        ): Result<$responseModelName> = withContext(Dispatchers.IO) {
            kotlin.runCatching {

                val response = httpClient
                    .${method.lowercase()}("$endpoint") {
                        url {
                            Json.encodeToJsonElement(request).jsonObject
                                .forEach {
                                    parameters.append(it.key, it.value.jsonPrimitive.content)
                                }
                        }
                    }

                response
                    .bodyAsText()
                    .let {
                        Json.decodeFromString<$responseModelName>(it)
                    }
            }
        }
    """.trimIndent()

    return "$pt1\n$pt2\n$pt3"
}

private object HEADClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy$name",
            content = ""
        )
    }
}

private object OPTIONSClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy$name",
            content = ""
        )
    }
}