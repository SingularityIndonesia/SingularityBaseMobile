package plugin.postman_client_generator.companion

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
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
    val clue: String?
) : TypeToken {
    override val value: String = "Number"
}

object ListType : TypeToken {
    override val value: String = "List"
}

object ObjectType : TypeToken {
    override val value: String = "Object"
}

fun resolveType(
    typeClue: String?
): TypeToken {
    when {
        isBoolean(typeClue) -> BooleanType
        isNumber(typeClue) -> NumberType(clue = typeClue)
        isObject(typeClue) -> ObjectType
        isList(typeClue) -> ListType
        else -> throw IllegalArgumentException("Unknown type $typeClue")
    }
    return StringType
}

fun resolveType(
    clue: JsonElement
): TypeToken {
    val type = when (clue) {
        is JsonPrimitive -> {
            when {
                clue.doubleOrNull != null -> NumberType(clue = clue.double.toString())
                clue.floatOrNull != null -> NumberType(clue = clue.float.toString())
                clue.booleanOrNull != null -> BooleanType
                clue.longOrNull != null -> NumberType(clue = clue.long.toString())
                clue.intOrNull != null -> NumberType(clue = clue.int.toString())
                clue.contentOrNull != null -> StringType
                else -> StringType
                /*else -> UnknownType*/
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