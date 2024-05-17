/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 17/05/2024 14:05
 * You are not allowed to remove the copyright.
 */
package plugin.postman_client_generator.companion

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive

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

fun isBoolean(
    clues: List<JsonElement?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && v.jsonPrimitive.booleanOrNull != null }
}

fun isNumber(
    clues: List<JsonElement?>
): Boolean {
    val mClue = clues
        .filterNotNull().mapNotNull {
            it.jsonPrimitive.contentOrNull
        }
        .ifEmpty { return false }

    val match = mClue
        .fold(true) { acc, v -> acc && numberPattern.matches(v) }

    return match
}

fun isObject(
    clues: List<JsonElement?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && v is JsonObject }
}

fun isList(
    clues: List<JsonElement?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && v is JsonArray }
}

fun isStringType(
    clues: List<JsonElement?>
): Boolean {
    return clues.filterNotNull()
        .fold(true) { acc, v -> acc && v.jsonPrimitive.contentOrNull != null }
}

