/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator.companion

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
val jsonFormatter = Json {
    prettyPrint = true
    explicitNulls = false
}

fun List<JsonObject>.compareMerge(): JsonElement {

    if (isEmpty())
        throw IllegalArgumentException("List is empty. Nothing to compare.")

    if (size == 1)
        return first()

    val keys = this
        .map {
            it.keys
        }
        .flatten()
        .removeDuplicates()

    val newValue = keys.map { key ->
        val values = this
            .map {
                // fixme: testing
                it.getOrDefault(key, null)
            }

        when (values.first()) {
            // compare child
            is JsonObject -> {
                (values as List<JsonObject>).compareMerge()
            }
            // combine list
            is JsonArray -> {
                (values as List<JsonArray>)
                    .fold(listOf<JsonElement>()) { acc, v ->
                        acc.plus(v)
                    }
                    .let {
                        jsonFormatter.encodeToJsonElement(it)
                    }
            }
            // primitive take longer content
            else -> {
                values
                    .fold(null as String?) { acc, v ->
                        val content = v?.jsonPrimitive?.contentOrNull

                        if ((content?.length ?: 0) > (acc?.length ?: 0))
                            content
                        else
                            acc
                    }
                    .let {
                        jsonFormatter.encodeToJsonElement(it)
                    }
            }
        }
    }

    val result = keys.zip(newValue)
        .toMap()

    return jsonFormatter.encodeToJsonElement(result)
}


fun List<String>.removeDuplicates(): List<String> {
    val new = mutableListOf<String>()
    forEach {
        if (!new.contains(it))
            new.add(it)
    }

    return new
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

