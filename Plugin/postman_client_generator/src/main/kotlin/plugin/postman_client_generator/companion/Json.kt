package plugin.postman_client_generator.companion

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

fun List<JsonObject>.compareMerge(): JsonElement {
    return fold(
        mapOf<String, JsonElement?>()
    ) { acc, v ->
        val keys = acc.keys + v.keys
        val values = keys.map {
            listOfNotNull(v[it], acc[it])
                .firstOrNull {
                    it !is JsonNull
                }
        }
        keys.zip(values)
            .toMap()
    }
        // ignore all keys with null values
        .filter {
            if (it.value == null)
                println("Warning: Cannot resolve parameter type for ${it.key} in ${this@compareMerge}.")
            it.value != null
        }
        // to json
        .let {
            Json.encodeToJsonElement(it)
        }
}