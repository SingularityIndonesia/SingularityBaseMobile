package plugin.convention.postmanclientgenerator

fun dataClassTemplate(
    name: String,
    params: List<Pair<String, String>>
): String {
    return listOfNotNull(
        """
@Serializable
data class $name (""",
        params
            .map {
                """
    @SerialName("${it.first}")
    val ${it.first}: ${it.second}?,
            """
            }
            .fold("") { acc, v -> "$acc$v" },
        """
)"""
    ).fold("") { acc, v -> acc + v }
}

fun clientTemplate(
    functionName: String,
    method: String,
    headerModelName: String?,
    headerModel: Header?,
    requestModelName: String?,
    requestModel: RequestModel?,
    responseModelName: String,
    responseModel: ResponseModel,
    pathArguments: List<String>,
    endpoint: String,
): String {

    val headerContent = headerModel?.print()
    val requestContent = requestModel?.print()
    val responseContent = responseModel.print()

    return listOfNotNull(
        """
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

suspend fun $functionName(
    httpClient: HttpClient""",
        headerContent
            ?.let {
                """,
    header: $headerModelName"""
            },
        requestContent
            ?.let {
                """,
    request: $requestModelName"""
            },
        pathArguments
            .ifEmpty { null }
            ?.map {
                """,
    $it: String"""
            }
            ?.fold("") { acc, v -> "$acc$v" },
        """
): Result<$responseModelName> = withContext(Dispatchers.IO) {
    kotlin.runCatching {
        httpClient
            .${method.lowercase()}("$endpoint") {
                url {""",
        headerContent?.let {
            """
                    headers {
                        Json.encodeToJsonElement(header).jsonObject.forEach { (key, value) ->
                            append(key, value.jsonPrimitive.content)
                        }
                    }
        """
        },
        requestContent?.let {
            """
                    Json.encodeToJsonElement(request).jsonObject
					    .forEach {
					        parameters.append(it.key, it.value.jsonPrimitive.content)
					    }
        """
        },
        """
                }
            }
            .bodyAsText()
            .let {
                Json.decodeFromString<$responseModelName>(it)
            }
    }
}""",
        headerContent?.let { "\n\n$it" },
        requestContent?.let { "\n\n$it" },
        responseContent.let { "\n\n$it" }
    )
        .fold("") { acc, v -> "$acc$v" }
}