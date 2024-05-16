package plugin.convention.postmanclientgenerator

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import plugin.convention.companion.ListType
import plugin.convention.companion.ObjectType
import plugin.convention.companion.printToFile
import plugin.convention.companion.removeNonAlphaNumeric
import plugin.convention.companion.resolveType
import java.io.File

@JvmInline
value class Context(
    val value: String
)

@Serializable
data class PostmanClient(
    val name: String,
    val nameSpace: String,
    val groupName: String,
    val content: String
) {
    fun generateFile(
        outputDir: File
    ): File {
        val content = "package $nameSpace\n\n${content}"
        return printToFile(
            outputDir = outputDir,
            fileName = "${name}.kt",
            content = content
        )
    }
}

/**
 * @param name class name, ex: "GETBooksResponse".
 * @param params list of key variable and type.
 *  ex: "name" to "String", "books" to "List<BookItems>", "author" to "Author".
 */

data class DataClass(
    val name: String,
    val params: List<Pair<String, String>>,
    val subClasses: List<DataClass>
) {
    fun print(): String {
        val firstLevelClass = dataClassTemplate(
            name = name,
            params = params,
        )
        val subClasses = subClasses
            .map { it.print() }
            .fold("") { acc, v -> "$acc\n\n$v" }
            // add indent
            .replace("\n", "\n\t")
        return if (subClasses.isEmpty())
            firstLevelClass
        else
            "$firstLevelClass {$subClasses\n}"
    }
}

data class ResponseModel(
    val name: String,
    val response: Postman.ResponseItem
) : DataClassDecoder by DataClassDecoderImpl() {
    fun print(): String {
        return decodeDataClass(
            name,
            response.body ?: "{}",
            "Response"
        ).print()
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
                                        // FIXME: multi dimensional object for request body not yet supported
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