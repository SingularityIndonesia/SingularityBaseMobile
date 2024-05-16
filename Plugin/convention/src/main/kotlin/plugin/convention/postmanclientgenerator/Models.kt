package plugin.convention.postmanclientgenerator

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import plugin.convention.companion.ListType
import plugin.convention.companion.ObjectType
import plugin.convention.companion.removeNonAlphaNumeric
import plugin.convention.companion.resolveType

sealed interface PayloadType
object TypeQuery : PayloadType
object TypeBodyJson : PayloadType

@JvmInline
value class Context(
    val value: String
)

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
    val request: Postman.Request,
    /*val queries: List<Postman.QueryItem>*/
) {
    companion object {

    }

    fun print(): String? {

        // validate
        val queryIsValid = !request.url?.query.isNullOrEmpty()
        val bodyIsValid = run {
            !request.body?.raw.isNullOrBlank() && request.body?.options?.raw?.language == "json"
        }

        // for now request only support query or body json
        val queries = request.let {
            if (queryIsValid)
                return@let request.url?.query?.filterNotNull()

            if (bodyIsValid)
                return@let request.body?.raw?.let { body ->
                    runCatching { Json.parseToJsonElement(body) }
                        .getOrElse { throw Error("error parsing $body") }
                        .jsonObject.map { entry ->
                            val value = resolveType(entry.value)
                                .fold(
                                    ifString = { entry.value.jsonPrimitive.content },
                                    ifNumber = { entry.value.jsonPrimitive.content },
                                    ifBoolean = { entry.value.jsonPrimitive.content },
                                    ifList = {
                                        // FIXME: support for this
                                        println("Warning: List argument is not yet supported.")
                                        entry.value.jsonArray.toString()
                                    },
                                    ifObject = {
                                        // FIXME: multi dimensional object not yet supported
                                        println("Warning: Multi dimensional object is not supported")
                                        entry.value.jsonObject.toString()
                                    }
                                )

                            Postman.QueryItem(
                                key = entry.key,
                                value = value
                            )
                        }
                }

            // none is valid
            null
        } ?: return null

        println("checking3 $queries")

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