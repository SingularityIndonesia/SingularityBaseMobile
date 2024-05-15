package plugin.convention.postmanclientgenerator

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import plugin.convention.companion.resolveType
import java.util.regex.Pattern

@Serializable
data class PostmanClient(
    val name: String,
    val content: String
)

data class ResponseModel(
    val name: String,
    val response: Postman.ResponseItem
) {
    fun print(): String {
        val pt1 = "@Serializable\ndata class $name("
        val pt2 = decodeResponseBodyParam(response.body ?: "{}")
        val pt3 = ")"
        return "$pt1\n$pt2\n$pt3"
    }

    private fun decodeResponseBodyParam(
        bodyJson: String
    ): String {
        val json = Json.parseToJsonElement(bodyJson).jsonObject
        val params = json.keys.map {
            "\t@SerialName(\"${it}\")" +
                    "\n\tval $it: ${resolveType(json[it]!!).value}?"
        }.joinToString(",\n\n")

        return params
    }
}

data class RequestModel(
    val name: String,
    val queries: List<Postman.QueryItem>
) {
    companion object {

    }

    fun print(): String {

        val pt1 = """
            @Serializable
            data class $name(
        """.trimIndent()

        val pt2 =
            queries
                .map {
                    "\t@SerialName(\"${it.key}\")" +
                            "\n\tval ${it.key}: ${resolveType(it.value!!).value}?"
                }
                .joinToString(",\n\n")

        val pt3 = "\n)"

        return "$pt1$pt2$pt3"
    }

}

sealed interface ClientGeneratorStrategy {
    fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request,
        response: Postman.ResponseItem
    ): PostmanClient
}

object CommonClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request,
        response: Postman.ResponseItem
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
            if (query.isEmpty())
                null
            else
                RequestModel(
                    name = requestModelName,
                    queries = query.filterNotNull()
                ).print()

        val responseModelName = "${name}Response"
        val responseModel =
            ResponseModel(
                name = responseModelName,
                response = response
            ).print()

        val function = generateFunction(
            functionName = name,
            method = method,
            requestModelName = if (requestModel != null) requestModelName else null,
            responseModelName = responseModelName,
            endpoint = path
        )

        val dependencies = """
            
            import io.ktor.client.HttpClient
            import io.ktor.client.request.*
            import io.ktor.client.statement.bodyAsText
            import kotlinx.coroutines.Dispatchers
            import kotlinx.coroutines.IO
            import kotlinx.serialization.Serializable
            import kotlinx.serialization.SerialName
            import kotlinx.coroutines.withContext
            import kotlinx.serialization.json.Json
            import kotlinx.serialization.json.encodeToJsonElement
            import kotlinx.serialization.json.jsonObject
            import kotlinx.serialization.json.jsonPrimitive
            
        """.trimIndent()

        val final = "$dependencies\n$function\n\n${requestModel ?: ""}\n\n$responseModel"

        return PostmanClient(
            name = name,
            content = final
        )
    }

    private fun generateFunction(
        functionName: String,
        method: String,
        requestModelName: String?,
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
                    "\n$it,"
                else
                    it
            }

        val pt3 = if (requestModelName != null)
            "\n\trequest: $requestModelName"
        else ""

        val pt4 = """
        
        ): Result<$responseModelName> = withContext(Dispatchers.IO) {
            kotlin.runCatching {

                val response = httpClient
                    .${method.lowercase()}("$endpoint")
    """.trimIndent()

        val pt5 = if (requestModelName == null)
            ""
        else
            """ {
                url {
                    Json.encodeToJsonElement(request).jsonObject
                        .forEach {
                            parameters.append(it.key, it.value.jsonPrimitive.content)
                        }
                    }
                }
        """.trimIndent()

        val pt6 = """


                response
                    .bodyAsText()
                    .let {
                        Json.decodeFromString<$responseModelName>(it)
                    }
            }
        }
    """.trimIndent()

        return "$pt1$pt2$pt3$pt4$pt5$pt6"
    }
}

object HEADClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request,
        response: Postman.ResponseItem
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy$name",
            content = ""
        )
    }
}

object OPTIONSClientGenerator : ClientGeneratorStrategy {
    override fun generateClient(
        contexts: List<Context>,
        name: String,
        request: Postman.Request,
        response: Postman.ResponseItem
    ): PostmanClient {
        // fixme
        return PostmanClient(
            name = "Dummy$name",
            content = ""
        )
    }
}