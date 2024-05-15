package plugin.convention.companion

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull


sealed interface TypeToken {
    val value: String
    fun <R> fold(
        ifString: (StringType) -> R,
        ifBoolean: (BooleanType) -> R,
        ifNumber: (NumberType) -> R,
        ifList: (ListType) -> R,
        ifObject: (ObjectType) -> R,
    ): R {
        return when (this) {
            is StringType -> ifString(this)
            is BooleanType -> ifBoolean(this)
            is NumberType -> ifNumber(this)
            is ListType -> ifList(this)
            is ObjectType -> ifObject(this)
        }
    }
}

object StringType : TypeToken {
    override val value: String = "String"
}

object BooleanType : TypeToken {
    override val value: String = "Boolean"
}

object NumberType : TypeToken {
    override val value: String = "Number"
}

object ListType : TypeToken {
    override val value: String = "List"
}

object ObjectType : TypeToken {
    override val value: String = "Object"
}

fun resolveType(
    typeClue: String
): TypeToken {
    when {
        isBoolean(typeClue) -> BooleanType
        isNumber(typeClue) -> NumberType
        isObject(typeClue) -> ObjectType
        isList(typeClue) -> ListType
    }
    return StringType
}

fun resolveType(
    clue: JsonElement
): TypeToken {
    val type = when (clue) {
        is JsonPrimitive -> {
            when {
                clue.doubleOrNull != null -> NumberType
                clue.floatOrNull != null -> NumberType
                clue.booleanOrNull != null -> BooleanType
                clue.longOrNull != null -> NumberType
                clue.intOrNull != null -> NumberType
                clue.contentOrNull != null -> StringType
                else -> StringType
            }
        }

        is JsonArray -> {
            ListType
        }

        is JsonObject -> {
            ObjectType
        }
    }
    return type
}