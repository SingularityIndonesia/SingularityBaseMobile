package plugin.convention

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.RequestModel.Companion.generateRequestModel
import plugin.convention.companion.addToSourceSet
import plugin.convention.companion.find
import plugin.convention.postmanclientgenerator.Postman
import java.io.File

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
                ?.map { path ->
                    path.replaceFirstChar { it.uppercase() }
                }
                ?.fold("") { acc, v -> "$acc$v" }
                ?: throw UnknownError("Fail to decode ${request.url} to name.")
        )

        val strategy = when (method) {
            "GET" -> GETClientGenerator
            "POST" -> POSTClientGenerator
            "PUT" -> PUTClientGenerator
            "PATCH" -> PATCHClientGenerator
            "DELETE" -> DELETEClientGenerator
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

private object POSTClientGenerator : ClientGeneratorStrategy {
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
            generateRequestModel(
                name = requestModelName,
                queryBlock = query.filterNotNull()
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
private value class Name(val value: String)

@JvmInline
private value class TypeName(val value: String)

private data class RequestModel(
    val name: String,
    val params: Map<Name, TypeName>
) {
    companion object {
        /**
         * ```
         * "query": [
         * 		{
         * 			"key": "sort_by",
         * 			"value": "name"
         * 		},
         * 		{
         * 			"key": "limit",
         * 			"value": "10"
         * 		},
         * 		{
         * 			"key": "page",
         * 			"value": "1"
         * 		},
         * 		{
         * 			"key": "q",
         * 			"value": "Super Man"
         * 		}
         * 	]
         * ```
         */
        fun generateRequestModel(
            name: String,
            queryBlock: List<Postman.QueryItem>
        ): RequestModel {

            val params: Map<Name, TypeName> =
                if (queryBlock.isEmpty())
                // put unit, cz dataclass require minimum of one parameter
                    mapOf(Name("unit") to TypeName("Unit"))
                else
                    queryBlock
                        .map {
                            Pair(
                                Name(it.key!!),
                                resolveType(it.value!!)
                            )
                        }
                        .let {
                            mapOf(*it.toTypedArray())
                        }

            return RequestModel(
                name = name,
                params = params
            )
        }

        fun resolveType(
            clue: String
        ): TypeName {
            // fixme: for now everything is string
            return TypeName("String")
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
            params
                .map {
                    "\tval ${it.key.value}: ${it.value.value}?"
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

    return """
        suspend fun $functionName(
            httpClient: HttpClient,
            request: $requestModelName
        ): Result<$responseModelName> = withContext(Dispatchers.IO) {
            kotlin.runCatching {

                val response = httpClient
                    .${method.lowercase()}("$endpoint") {
                        url {
                            Json.encodeToJsonElement(request).jsonObject
                                .forEach {
                                    parameters.append(it.key, it.value)
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
}

private object GETClientGenerator : ClientGeneratorStrategy {
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

private object PUTClientGenerator : ClientGeneratorStrategy {
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

private object PATCHClientGenerator : ClientGeneratorStrategy {
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

private object DELETEClientGenerator : ClientGeneratorStrategy {
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