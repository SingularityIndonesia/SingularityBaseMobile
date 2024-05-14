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
import org.gradle.internal.impldep.org.apache.ivy.plugins.namespace.Namespace
import plugin.convention.RequestModel.Companion.generateRequestModel
import plugin.convention.companion.addToSourceSet
import java.io.File

@Serializable
private data class PostmanProtoClient(
    val context: List<String>,
    val name: String,
    val request: JsonObject,
)

@Serializable
private data class PostmanClient(
    val name: String,
    val content: String
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
            scanPostmanFiles(
                target.projectDir
            )

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
        val file = File(outputDir,"${postmanClient.name}.kt")
        file.parentFile.mkdir()

        val content = "package $namespace\n\n${postmanClient.content}"
        file.writeText(content)
    }

    private fun setup(
        project: Project
    ) {
        project.addToSourceSet(targetDir)
    }

    private fun scanPostmanFiles(
        sourceDir: File,
    ): Sequence<File> {
        return sourceDir.listFiles()
            ?.asSequence()
            ?.mapNotNull { file ->
                if (file.isDirectory) {
                    // If it's a directory, recursively scan it
                    scanPostmanFiles(file)
                } else {
                    if (file.name.contains("postman_collection.json"))
                        sequenceOf(file)
                    else
                        null
                }
            }
            ?.flatten()
            ?: sequenceOf()
    }

    private fun generateClients(
        file: File
    ): Sequence<PostmanClient> {
        val content = file.readText()
        val json =
            Json.decodeFromString<JsonObject>(content)
        val protoClients =
            run {
                val item =
                    json["item"]?.jsonArray ?: throw IllegalArgumentException("item is undefined")
                val itemName = file.name.split(".").first().replaceFirstChar { it.uppercase() }
                proceedItems(listOf(itemName), item)
            }
        val clients =
            protoClients.map(::generateClient)

        return clients
    }

    private fun proceedItems(
        context: List<String>,
        items: JsonArray
    ): Sequence<PostmanProtoClient> {

        val result = items
            .asSequence()
            .map { json ->
                json as JsonObject
                proceedItem(context, json)
            }
            .flatten()

        return result
    }

    private fun proceedItem(
        context: List<String>,
        json: JsonObject
    ): List<PostmanProtoClient> {
        val results = mutableListOf<PostmanProtoClient>()

        if (json.keys.contains("item")) {
            val subItems =
                json["item"]?.jsonArray ?: throw UnknownError("subItem is undefined")
            val subItemContext =
                json["name"]?.jsonPrimitive?.content
                    ?.replaceFirstChar { it.uppercase() }
                    ?.let { context.plus(it) }
                    ?: throw UnknownError("subItemName is undefined")
            results.addAll(proceedItems(subItemContext, subItems))
        } else {
            val requestObj =
                json["request"]?.jsonObject
                    ?: throw IllegalArgumentException("request is undefined")
            val requestName =
                json["name"]?.jsonPrimitive?.content
                    ?: throw IllegalArgumentException("name is undefined")
            val protoClient = PostmanProtoClient(
                context, requestName, requestObj
            )

            results.add(protoClient)
        }

        return results
    }

    private fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        val request =
            protoClient.request
        val method =
            request["method"]?.jsonPrimitive?.content
                ?: throw NullPointerException("method is null: \n$request")

        val strategy = when (method) {
            "POST" -> POSTClientGenerator
            "GET" -> GETClientGenerator
            "PUT" -> PUTClientGenerator
            "PATCH" -> PATCHClientGenerator
            "DELETE" -> DELETEClientGenerator
            "HEAD" -> HEADClientGenerator
            "OPTIONS" -> OPTIONSClientGenerator
            else -> throw IllegalArgumentException("Unknown method $method")
        }

        return strategy.generateClient(protoClient)
    }
}


private sealed interface ClientGeneratorStrategy {
    fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient
}

private object POSTClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        val url = protoClient.request["url"]?.jsonObject
            ?: throw NullPointerException("url is null: \n$protoClient")

        /**
         * ```
         * "path": [
         * 	"books",
         * 	":bookID",
         * 	"comments"
         * ]
         * Into : books/${bookID}/comments
         */
        val path = url["path"]?.jsonArray
            ?.map {
                val p = it.jsonPrimitive.content
                if (p.contains(":")) {
                    val pureName = p.replaceFirstChar { "" }
                    "\${$pureName}"
                } else {
                    p
                }
            }
            ?.fold("") { acc, v -> "$acc/$v" }
            ?: throw IllegalArgumentException("path is null:\n $protoClient")

        val method = protoClient.request["method"]?.jsonPrimitive?.content
            ?: throw NullPointerException("method is null:\n$protoClient")

        /**
         * ```
         * "path": [
         * 	"books",
         * 	":bookID",
         * 	"comments"
         * ],
         * "method": "POST"
         * ```
         * Into POSTBooksComments.
         * Path variable will be ignored
         */
        val functionName =
            method
                .plus(
                    url["path"]?.jsonArray
                        ?.filter {
                            !it.jsonPrimitive.content.contains(":")
                        }
                        ?.fold("") { acc, v ->
                            "$acc${v.jsonPrimitive.content.replaceFirstChar { it.uppercase() }}"
                        }
                )

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
        val query = url["query"]?.jsonArray ?: buildJsonArray { }
        val requestModelName = "${functionName}Request"
        val requestModel =
            generateRequestModel(
                name = requestModelName,
                queryBlock = query
            )

        val responseModelName = "${functionName}Response"

        val function = generateFunction(
            functionName = functionName,
            method = method,
            requestModelName = requestModelName,
            responseModelName = responseModelName,
            endpoint = path
        )

        val final = "${requestModel.print()}\n\n$function"

        return PostmanClient(
            name = functionName,
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
            queryBlock: JsonArray
        ): RequestModel {

            val params: Map<Name, TypeName> =
                if (queryBlock.isEmpty())
                // put unit, cz dataclass require minimum of one parameter
                    mapOf(Name("unit") to TypeName("Unit"))
                else
                    queryBlock
                        .map {
                            it as JsonObject
                            Pair(
                                Name(it["key"]!!.jsonPrimitive.content),
                                resolveType(it["value"]!!.jsonPrimitive.content)
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
        protoClient: PostmanProtoClient
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy${protoClient.name}",
            content = ""
        )
    }
}

private object PUTClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy${protoClient.name}",
            content = ""
        )
    }
}

private object PATCHClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy${protoClient.name}",
            content = ""
        )
    }
}

private object DELETEClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy${protoClient.name}",
            content = ""
        )
    }
}

private object HEADClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy${protoClient.name}",
            content = ""
        )
    }
}

private object OPTIONSClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        protoClient: PostmanProtoClient
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy${protoClient.name}",
            content = ""
        )
    }
}