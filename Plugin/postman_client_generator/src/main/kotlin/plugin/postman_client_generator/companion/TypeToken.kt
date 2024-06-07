/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator.companion

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull


sealed interface TypeToken {
    val value: String
    fun <R> fold(
        ifString: (StringType) -> R,
        ifBoolean: (BooleanType) -> R,
        ifNumber: (NumberType) -> R,
        ifList: (ListType) -> R,
        ifObject: (ObjectType) -> R,
        /*ifUnknown: () -> R*/
    ): R {
        return when (this) {
            is StringType -> ifString(this)
            is BooleanType -> ifBoolean(this)
            is NumberType -> ifNumber(this)
            is ListType -> ifList(this)
            is ObjectType -> ifObject(this)
            /*is UnknownType -> ifUnknown()*/
        }
    }
}

/*object UnknownType: TypeToken {
    override val value: String = "Unknown"
}*/

object StringType : TypeToken {
    override val value: String = "String"
}

object BooleanType : TypeToken {
    override val value: String = "Boolean"
}

data class NumberType(
    val clues: List<String?>,
) : TypeToken {
    override val value: String = "Number"
}

object ListType : TypeToken {
    override val value: String = "List"
}

object ObjectType : TypeToken {
    override val value: String = "Object"
}

fun resolveStringType(
    typeClues: List<String?>
): TypeToken {
    return when {
        isBoolean(typeClues) -> BooleanType
        isNumber(typeClues) -> NumberType(
            clues = typeClues
        )

        isObject(typeClues) -> ObjectType
        isList(typeClues) -> ListType
        else -> StringType
    }
}

fun resolveJsonType(
    clues: List<JsonElement?>
): TypeToken {
    if (clues.isEmpty())
        throw IllegalArgumentException("Please provide at least one item for clues.\nClues: $clues")

    val type = when {
        isObject(clues) -> ObjectType
        isList(clues) -> ListType
        isBoolean(clues) -> BooleanType
        isNumber(clues) -> NumberType(
            clues = clues.map {
                it?.jsonPrimitive?.let {
                    it.intOrNull ?: it.longOrNull ?: it.floatOrNull ?: it.doubleOrNull
                    ?: it.contentOrNull
                }?.toString()
            },
        )

        isStringType(clues) -> StringType
        else -> throw UnknownError("Fail to resolve types of $clues")
    }
    return type
}