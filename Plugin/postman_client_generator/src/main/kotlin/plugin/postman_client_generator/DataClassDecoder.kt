package plugin.postman_client_generator

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import plugin.postman_client_generator.companion.ListType
import plugin.postman_client_generator.companion.ObjectType
import plugin.postman_client_generator.companion.removeNonAlphaNumeric
import plugin.postman_client_generator.companion.resolveType

interface DataClassDecoder {

    /**
     * @param subClassSuffix in case you want to add suffix to the class name,
     */
    fun decodeDataClass(
        name: String,
        jsonString: String,
        subClassSuffix: String?
    ): DataClass
}

class DataClassDecoderImpl : DataClassDecoder {
    override fun decodeDataClass(
        name: String,
        jsonString: String,
        subClassSuffix: String?
    ): DataClass {
        val json = kotlin.runCatching {
            Json.parseToJsonElement(jsonString)
                .jsonObject
        }.getOrElse { throw Error("parsing error $name $jsonString") }

        val params = run {
            json
                .map {
                    val valueType = resolveType(it.value)
                    val listItemType = if (valueType == ListType) {
                        resolveType(it.value.jsonArray.first())
                    } else
                        null
                    it.key to valueType to listItemType
                }
                // todo: support for multi dimensional list
                //  ex: List<List<*>>
                .onEach {
                    if (it.second == ListType) {
                        val message =
                            "Multi dimensional list is not yet supported.\n Object is multi dimensional list: ${json[it.first.first]}"
                        throw IllegalArgumentException(message)
                    }
                }
        }

        val subClasses = run {
            val classTypeDecoded = run {
                params
                    .filter {
                        it.first.second is ObjectType
                    }
                    .map {
                        val mName =
                            it.first.first.removeNonAlphaNumeric()
                                .replaceFirstChar { c -> c.uppercase() } + (subClassSuffix ?: "")
                        val mJson = json[it.first.first]!!.toString()
                        decodeDataClass(
                            mName,
                            mJson,
                            subClassSuffix
                        )
                    }
            }

            val listTypesSubClasses = run {
                params
                    .filter {
                        it.second == ObjectType
                    }
                    .map {
                        // fixme boiler plate
                        val objName = it.first.first
                            .removeNonAlphaNumeric()
                            .replaceFirstChar { c -> c.uppercase() }
                            .let {
                                "${it}Item${subClassSuffix ?: ""}"
                            }

                        val mJson = json[it.first.first]!!.jsonArray.first().jsonObject.toString()
                        decodeDataClass(
                            objName,
                            mJson,
                            subClassSuffix
                        )
                    }
            }

            classTypeDecoded.plus(listTypesSubClasses)
        }

        val paramsList = run {
            params
                .map {
                    val stringToken = when {
                        it.first.second == ObjectType -> {
                            val mName = it.first.first
                                .removeNonAlphaNumeric()
                                .replaceFirstChar { c -> c.uppercase() }
                                .let {
                                    "$it${subClassSuffix ?: ""}"
                                }
                            mName
                        }
                        // list object
                        it.second == ObjectType -> {
                            // fixme boiler plate
                            val objName = it.first.first
                                .removeNonAlphaNumeric()
                                .replaceFirstChar { c -> c.uppercase() }
                                .let {
                                    "${it}Item${subClassSuffix ?: ""}"
                                }
                            objName
                        }
                        // multi dimensional list type
                        it.second == ListType -> {
                            val message =
                                "Multi dimensional list is not yet supported.\n Object is multi dimensional list: ${json[it.first.first]}"
                            throw IllegalArgumentException(message)
                        }
                        // list primitives
                        it.second != null && it.second != ObjectType && it.second != ListType -> {
                            "List<${it.second?.value}?>"
                        }

                        else -> it.first.second.value
                    }

                    it.first.first to stringToken
                }
        }

        return DataClass(
            name = name,
            params = paramsList,
            subClasses = subClasses
        )
    }

}