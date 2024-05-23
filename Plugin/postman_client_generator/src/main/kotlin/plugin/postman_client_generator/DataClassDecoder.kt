/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.postman_client_generator

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import plugin.postman_client_generator.companion.ListType
import plugin.postman_client_generator.companion.NumberType
import plugin.postman_client_generator.companion.NumberTypeResolverStrategy
import plugin.postman_client_generator.companion.ObjectType
import plugin.postman_client_generator.companion.compareMerge
import plugin.postman_client_generator.companion.jsonFormatter
import plugin.postman_client_generator.companion.removeNonAlphaNumeric
import plugin.postman_client_generator.companion.resolveJsonType

val commentRemoverPattern = Regex("//.*$", RegexOption.MULTILINE)

interface DataClassDecoder {

    /**
     * @param subClassSuffix in case you want to add suffix to the class name,
     * @param jsonString Is json string to parse. Class decoder only support list of json object.
     *   ex: [ {"name":"dahyun", "is_cute":true} {"age":20}]; Those classes will be compared into one json object {"name": "dahyun", "is_cute":true, "age": 20}
     */
    fun decodeDataClass(
        name: String,
        jsonString: List<String>,
        numberTypeResolverStrategy: NumberTypeResolverStrategy,
        subClassSuffix: String?
    ): DataClass
}

class DataClassDecoderImpl : DataClassDecoder {
    override fun decodeDataClass(
        name: String,
        jsonString: List<String>,
        numberTypeResolverStrategy: NumberTypeResolverStrategy,
        subClassSuffix: String?
    ): DataClass {

        val comparedJson = jsonString
            // remove comment
            .map {
                it.replace(commentRemoverPattern, "")
            }
            .map {
                runCatching {
                    jsonFormatter.parseToJsonElement(it).jsonObject
                }.getOrElse {e ->
                    throw Error("$it $e")
                }
            }
            .compareMerge()
            .jsonObject

        val params = run {
            comparedJson
                .map {
                    val valueType = resolveJsonType(listOf(it.value))
                    val listItemType = if (valueType == ListType) {
                        resolveJsonType(it.value.jsonArray)
                    } else
                        null
                    it.key to valueType to listItemType
                }
                // todo: support for multi dimensional list
                //  ex: List<List<*>>
                .onEach {
                    if (it.second == ListType) {
                        val message =
                            "Multi dimensional list is not yet supported.\n Object is multi dimensional list: ${comparedJson[it.first.first]}"
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

                        // json already compared so we can list it up a single item.
                        val mJson = listOf(comparedJson[it.first.first]!!.toString())
                        decodeDataClass(
                            mName,
                            mJson,
                            numberTypeResolverStrategy,
                            subClassSuffix,
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

                        val mJson =
                            comparedJson[it.first.first]!!.jsonArray
                                .map { it.jsonObject.toString() }

                        decodeDataClass(
                            objName,
                            mJson,
                            numberTypeResolverStrategy,
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
                        // number type
                        // fixme:
                        it.first.second is NumberType -> {
                            val clues = (it.first.second as NumberType).clues
                            numberTypeResolverStrategy.predictedType(
                                clues
                            )
                        }
                        // object type
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
                            "List<$objName?>"
                        }
                        // multi dimensional list type
                        it.second == ListType -> {
                            val message =
                                "Multi dimensional list is not yet supported.\n Object is multi dimensional list: ${comparedJson[it.first.first]}"
                            throw IllegalArgumentException(message)
                        }
                        // list primitives
                        it.second != null && it.second != ObjectType && it.second != ListType -> {
                            val itemType =
                                // fixme:
                                if (it.second is NumberType) {
                                    val clues = (it.first.second as NumberType).clues
                                    numberTypeResolverStrategy.predictedType(
                                        clues
                                    )
                                } else {
                                    it.second?.value
                                }
                            "List<$itemType?>"
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