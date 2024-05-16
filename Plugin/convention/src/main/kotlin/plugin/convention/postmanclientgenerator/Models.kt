package plugin.convention.postmanclientgenerator

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
) : DataClassDecoder by DataClassDecoderImpl() {

    // todo: query is currently not yet supported
    fun print(): String? {
        val responseBody = run {
            if (request.body?.raw.isNullOrBlank() || request.body?.options?.raw?.language != "json")
                return@run null
            else
                request.body.raw?.let {
                    decodeDataClass(
                        name,
                        it,
                        "Request"
                    )
                }
        }

        return responseBody?.print()
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