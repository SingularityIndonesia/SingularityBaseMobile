package plugin.convention.postmanclientgenerator

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import plugin.convention.companion.ListType
import plugin.convention.companion.ObjectType
import plugin.convention.companion.removeNonAlphaNumeric
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
        return decodeDataClass(
            name,
            response.body ?: "{}"
        )
    }

    private fun decodeDataClass(
        name: String,
        body: String
    ): String {
        val pt1 = "@Serializable\ndata class $name("
        val pt2 = decodeResponseBodyParam(body)
        val pt3 = ")"

        val subclasses = decodeSubClasses(body)

        val pt4 = if (subclasses.isEmpty())
            ""
        else
            subclasses.joinToString("\n\n")
                // add extra indent
                .replace("\n", "\n\t")
                .let {
                    "{\n\n\t$it\n}"
                }
        return "$pt1\n$pt2\n$pt3$pt4"
    }

    private fun decodeResponseBodyParam(
        bodyJson: String
    ): String {
        val json = Json.parseToJsonElement(bodyJson)
            .let {
                if (it is JsonArray)
                    it.first().jsonObject
                else
                    it.jsonObject
            }

        val params = json.keys.map { key ->
            val type = resolveType(json[key]!!)
                .fold(
                    ifString = { it.value },
                    ifNumber = { it.value },
                    ifBoolean = { it.value },
                    ifList = {
                        val arr = json[key]!!.jsonArray
                        if (arr.isEmpty())
                            return@fold "List<Any?>"

                        val listItemType = resolveType(arr.first())
                            .fold(
                                ifString = { "String" },
                                ifNumber = { "Number" },
                                ifBoolean = { "Boolean" },
                                ifList = {
                                    throw IllegalArgumentException("Multi dimensional list is not supported. Error at: $arr")
                                },
                                ifObject = {
                                    key.removeNonAlphaNumeric()
                                        .replaceFirstChar { it.uppercase() } + "Response"
                                }
                            )
                        "List<${listItemType}?>"
                    },
                    ifObject = {
                        key.removeNonAlphaNumeric().replaceFirstChar { it.uppercase() } + "Response"
                    }
                )

            "\t@SerialName(\"${key}\")" +
                    "\n\tval $key: ${type}?"
        }.joinToString(",\n\n")

        return params
    }

    private fun decodeSubClasses(
        jsonBody: String
    ): List<String> {
        val json = Json.parseToJsonElement(jsonBody)
            .let {
                if (it is JsonArray) {
                    it.first().jsonObject
                } else {
                    it.jsonObject
                }
            }
        val subClassesMap = json.keys
            .filter { key ->
                val type = resolveType(json[key]!!)
                type == ObjectType || type == ListType
            }
            .map { key ->
                key to json[key]!!
            }

        val subClasses = subClassesMap
            .map { (key, value) ->
                decodeDataClass(
                    key.removeNonAlphaNumeric().replaceFirstChar { it.uppercase() } + "Response",
                    value.toString()
                )
            }

        val subClassesSubClasses = subClassesMap
            .map {
                decodeSubClasses(it.second.toString())
            }

        return subClasses.plus(subClassesSubClasses.flatten())
    }
}

data class RequestModel(
    val name: String,
    val queries: List<Postman.QueryItem>
) {
    companion object {

    }

    fun print(): String? {
        if (queries.isEmpty())
            return null

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

data class Header(
    val name: String,
    val headers: List<Postman.HeaderItem>
) {
    fun print(): String? {

        if (headers.isEmpty())
            return null

        val pt1 = """
            @Serializable
            data class ${name}(
        """.trimIndent()

        // todo: header is currently only support string
        val pt2 = headers.map {
            "\t@SerialName(\"${it.key}\")" +
                    "\n\tval ${it.key?.removeNonAlphaNumeric()}: String?"
        }.joinToString(",\n\n")

        val pt3 = ")"

        return "$pt1\n$pt2\n$pt3"
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

        val headers = request.header?.filterNotNull() ?: listOf()
        val headerModelName = "${name}Header"

        val headerModel =
            Header(
                name = headerModelName,
                headers = headers
            ).print()

        val function = generateFunction(
            functionName = name,
            method = method,
            headerModelName = if (headerModel != null) headerModelName else null,
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

        val final = dependencies
            .plus("\n\n$function")
            .plus(headerModel?.let { "\n\n$it" } ?: "")
            .plus(requestModel?.let { "\n\n$it" } ?: "")
            .plus("\n\n$responseModel")

        return PostmanClient(
            name = name,
            content = final
        )
    }

    private fun generateFunction(
        functionName: String,
        method: String,
        headerModelName: String?,
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

        val pt3 = if (headerModelName != null)
            "\n\theader: $headerModelName"
        else
            ""

        val pt4 = if (requestModelName != null)
            "\n\trequest: $requestModelName"
        else ""

        val pt5 =
            """
        
                ): Result<$responseModelName> = withContext(Dispatchers.IO) {
                    kotlin.runCatching {
        
                        val response = httpClient
                            .${method.lowercase()}("$endpoint")
            """.trimIndent()

        val pt6 = if (requestModelName == null && headerModelName == null)
            ""
        else {
            val x1 =
                """
                    {
                        url {
                """.trimIndent()
                    .replace("\n","\n\t\t\t")
            val x2 = if (headerModelName == null)
                ""
            else
                """
                    
                    headers {
                        Json.encodeToJsonElement(header).jsonObject.forEach { (key, value) ->
                            append(key, value.jsonPrimitive.content)
                        }
                    }
                """.trimIndent()
                    .replace("\n","\n\t\t\t\t\t")

            val x3 = if (requestModelName == null)
                ""
            else
                """
                    
                    Json.encodeToJsonElement(request).jsonObject
                        .forEach {
                            parameters.append(it.key, it.value.jsonPrimitive.content)
                        }
                """.trimIndent()
                    .replace("\n","\n\t\t\t\t\t")

            val x4 =
                """
                    
                        }
                    }
                """.trimIndent()
                    .replace("\n","\n\t\t\t")

            "$x1$x2$x3$x4"
        }

        val pt7 = """


                response
                    .bodyAsText()
                    .let {
                        Json.decodeFromString<$responseModelName>(it)
                    }
            }
        }
    """.trimIndent()

        return "$pt1$pt2$pt3$pt4$pt5$pt6$pt7"
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
            content = "// Head is not yet supported"
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
            content = "// Options is not yet supported"
        )
    }
}