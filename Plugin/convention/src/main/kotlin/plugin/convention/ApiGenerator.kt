/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package plugin.convention

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File

class ApiGenerator : Plugin<Project> {

    private val targetDir = "build/generated/kotlin/apigenerator/"

    override fun apply(target: Project) {

        // setup
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain {
                    kotlin.srcDir(targetDir)
                }
            }
        }

        // exec
        execute(target)
    }

    private fun execute(
        target: Project
    ) {
        val namespace = target.group
            .toString()
            .lowercase()

        val projectDir = target.projectDir
        val filePaths = scanDirectory(projectDir, projectDir)

        filePaths

            // finding all contract
            .filter { it.endsWith("apicontract.json") }
            .asSequence()
            .map { File(projectDir, it) }

            // evaluate each
            .onEach { file ->
                val content = file.readText()
                val json = Json.decodeFromString<JsonObject>(content)
                evaluate(
                    namespace,
                    projectDir,
                    json
                )
            }

            // execute sequence
            .toList()
    }

    private fun scanDirectory(directory: File, projectDir: File): List<String> {
        val filePaths = mutableListOf<String>()

        val files = directory.listFiles()
        files?.forEach { file ->
            if (file.isDirectory) {
                // If it's a directory, recursively scan it
                filePaths.addAll(scanDirectory(file, projectDir))
            } else {
                // Add the relative path of the file with respect to the project directory to the list
                val relativePath = file.relativeTo(projectDir).path
                filePaths.add(relativePath)
            }
        }

        return filePaths
    }

    private fun evaluate(
        namespace: String,
        projectDir: File,
        json: JsonObject,
    ) {
        generateResponseModel(
            namespace,
            projectDir,
            json
        )

        generateRequestModel(
            namespace,
            projectDir,
            json
        )

        generateClient(
            namespace,
            projectDir,
            json
        )
    }

    private fun generateResponseModel(
        namespace: String,
        outputDir: File,
        json: JsonObject
    ) {
        val context = json["context"].toString().replace("\"", "")
        val methods = json["methods"]?.jsonArray ?: return // no method found

        val responseModels = methods
            .map {
                it as JsonObject

                val reference = it["response"]
                    ?.jsonObject

                val schema = reference?.get("schema")
                    ?.jsonObject ?: throw NullPointerException("Schema for response is null")

                val type = reference?.get("type")
                    .toString()
                    .replace("\"", "")

                val method = it["method"].toString().replace("\"", "")

                val objectName = when (type) {
                    "object" -> {
                        "$context${method}Response"
                    }

                    "list" -> {
                        reference?.get("name").toString()
                            .replace("\"", "")
                            .let {
                                "$context$method${it}Response"
                            }
                    }

                    else -> throw IllegalArgumentException("Type $type is not supported for response.")
                }

                val fileContent = toResponseDataClass(
                    objectName,
                    schema
                )

                val fileName = "$objectName.kt"

                fileName to fileContent
            }

            // attach package name
            .map {
                val newValue = """
                    package $namespace.model
                    
                    import kotlinx.serialization.Serializable
                    
                """.trimIndent()

                it.first to "$newValue\n${it.second}"
            }


        responseModels.forEach {
            val outputFile = File(
                outputDir,
                "${targetDir}model/${it.first}"
            )
            outputFile.parentFile.mkdirs()
            outputFile.writeText(
                it.second
            )
        }
    }

    // fixme: response tidak bisa menggunakan knownSystemKeys karena ambigu dengan kontrak key
    private val knownSystemKeys = listOf("type", "value", "values")
    private val knownTypes = listOf("String", "Int", "Boolean", "Float", "Double", "List")
    private fun toResponseDataClass(
        objectName: String,
        schema: JsonObject
    ): String {

        // evaluation
        val properties = schema.keys.toList()
            .filter { !knownSystemKeys.contains(it) }

        val propertyTypes = properties
            .map { key ->
                val prop = schema[key]?.jsonObject
                val type = prop?.get("type")
                    .toString()
                    .replace("\"", "")

                val name = prop?.get("name")
                    .toString()
                    .replace("\"", "")

                when (type) {
                    "object" -> {
                        key.replaceFirstChar { it.toString().uppercase() }
                            .let {
                                "${it}Response"
                            }
                    }

                    "list" -> {
                        "List<${name}Response>"
                    }

                    else -> toActualType(type)
                }

            }

        val propVsType = properties.zip(propertyTypes)

        // generate class
        var dataClass = run {
            val pt1 =
                """
                    @Serializable
                    data class $objectName(
                """.trimIndent()
            val pt2 = propVsType.map {
                "val ${it.first}: ${it.second}"
            }.fold("") { acc, v ->
                "$acc\n    $v?,"
            }
            val pt3 = "\n)"

            pt1 + pt2 + pt3
        }

        val subObject = propVsType
            .filter { !knownTypes.contains(it.second) }
            .map {
                val type = it.second
                    // in case of type list
                    .replace("List<", "")
                    .replace(">", "")

                val obj = schema[it.first]?.jsonObject
                    ?.get("schema")?.jsonObject
                    ?: throw NullPointerException("data for ${it.first} is null")

                toResponseDataClass(
                    type,
                    obj
                )
            }
            .fold("") { acc, v ->
                "$acc\n$v"
            }

        if (subObject.isNotEmpty()) {
            val subObj =
                """
                    {
                        $subObject
                    }
                """.trimIndent()

            dataClass = "$dataClass\n$subObj"
        }


        return dataClass
    }

    private fun toActualType(
        type: String
    ): String {
        return when (type) {
            "string" -> "String"
            "int" -> "Int"
            "boolean" -> "Boolean"
            "float" -> "Float"
            "double" -> "Double"
            else -> type
        }
    }

    private fun generateRequestModel(
        namespace: String,
        outputDir: File,
        json: JsonObject
    ) {
        val context = json["context"].toString().replace("\"", "")
        val methods = json["methods"]?.jsonArray ?: return // no method f

        val requestModels = methods
            .map {
                it as JsonObject

                val method = it["method"].toString().replace("\"", "")
                val requestModelName = "$context${method}Request"

                val requestModel = it["request"]
                    ?.jsonObject
                    ?.let {
                        val keyVsType = run {
                            val keys = it.keys.toList()
                            val optionalTypes = it.values.map {
                                it as JsonObject
                                it["optional"].toString()
                                    .replace("\"", "")
                                    .toBoolean()
                                    .let {
                                        if (it)
                                            "?"
                                        else
                                            ""
                                    }
                            }
                            val types = it.values
                                .map {
                                    it as JsonObject
                                    it["type"].toString()
                                        .replace("\"", "")
                                        .let {
                                            when (it) {
                                                "list" -> throw IllegalArgumentException("List request not yet supported.")
                                                else -> it.replaceFirstChar {
                                                    it.toString().uppercase()
                                                }
                                            }
                                        }
                                }
                                .zip(optionalTypes)
                                .flatMap {
                                    listOf("${it.first}${it.second}")
                                }

                            keys.zip(types)
                        }

                        val pt1 = """
                            package $namespace.request
                            
                            import kotlinx.serialization.Serializable
                            
                            @Serializable
                            data class $requestModelName(
                                val unit: Unit = Unit,
                        """.trimIndent()

                        val pt2 = keyVsType
                            .map {
                                "\tval ${it.first}: ${it.second}"
                            }
                            .fold("") { acc, v ->
                                "$acc\n$v,"
                            }

                        val pt3 = """
                            
                            )
                        """.trimIndent()

                        "$pt1$pt2$pt3"
                    }

                requestModelName to requestModel
            }

        requestModels.forEach {
            val fileName = "${it.first}.kt"
            val outputFile = File(
                outputDir,
                "${targetDir}request/${fileName}"
            )
            outputFile.parentFile.mkdirs()
            outputFile.writeText(
                it.second ?: ""
            )
        }
    }

    private fun generateClient(
        namespace: String,
        outputDir: File,
        json: JsonObject
    ) {
        val context = json["context"].toString().replace("\"", "")
        val methods = json["methods"]?.jsonArray ?: return // no method f
        val endpoint = json["endpoint"].toString().replace("\"", "")
        val fileName = "${context}Client.kt"

        val clients = methods
            .map {
                it as JsonObject

                val method = it["method"].toString().replace("\"", "").lowercase()
                val functionName = method.replaceFirstChar { it.uppercase() }
                    .let { m ->
                        "$context$m"
                    }

                val responseModelName = it["response"]
                    ?.jsonObject
                    ?.get("type")
                    .toString().replace("\"", "")
                    .let { type ->
                        when (type) {
                            "object" -> {
                                "$context${method.uppercase()}Response"
                            }

                            "list" -> {
                                val name = it["response"]
                                    ?.jsonObject
                                    ?.get("name")
                                    .toString().replace("\"", "")

                                "List<$context${method.uppercase()}${name}Response>"
                            }

                            else -> throw IllegalArgumentException("Type $type is not supported for response.")
                        }
                    }

                val requestModelName = "$context${method.uppercase()}Request"

                generateApiClient(
                    functionName,
                    method,
                    endpoint,
                    responseModelName,
                    requestModelName
                )
            }
            .fold("") { acc, v ->
                "$acc\n\n$v"
            }

            // attach package name and dependencies
            .let {
                val modelDependencies = methods
                    .map {
                        it as JsonObject
                        val method = it["method"].toString().replace("\"", "").uppercase()
                        val responseModel = it["response"]
                            ?.jsonObject
                            ?.get("type")
                            .toString().replace("\"", "")
                            .let { type ->
                                when (type) {
                                    "object" -> {
                                        "$context${method.uppercase()}Response"
                                    }

                                    "list" -> {
                                        val name = it["response"]
                                            ?.jsonObject
                                            ?.get("name")
                                            .toString().replace("\"", "")

                                        "$context${method.uppercase()}${name}Response"
                                    }

                                    else -> throw IllegalArgumentException("Type $type is not supported for response.")
                                }
                            }
                        "import $namespace.model.$responseModel"
                    }
                    .fold("") { acc, v ->
                        "$acc\n$v"
                    }

                val requestDependencies = methods
                    .map {
                        it as JsonObject
                        val method = it["method"].toString().replace("\"", "").uppercase()
                        val requestModelName = "$context${method}Request"
                        "import $namespace.request.$requestModelName"
                    }
                    .fold("") { acc, v ->
                        "$acc\n$v"
                    }

                val pt1 = """
                    package $namespace
                    
                    import io.ktor.client.HttpClient
                    import io.ktor.client.request.*
                    import io.ktor.client.statement.bodyAsText
                    import kotlinx.coroutines.Dispatchers
                    import kotlinx.coroutines.IO
                    import kotlinx.coroutines.withContext
                    import kotlinx.serialization.json.Json
                    import kotlinx.serialization.json.encodeToJsonElement
                    import kotlinx.serialization.json.jsonObject
                """.trimIndent()

                val pt2 = modelDependencies
                val pt3 = requestDependencies
                val pt4 = it

                "$pt1$pt2$pt3$pt4"
            }

        val outputFile = File(
            outputDir,
            "${targetDir}/${fileName}"
        )
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            clients
        )
    }

    private fun generateApiClient(
        functionName: String,
        method: String,
        endpoint: String,
        responseModelName: String,
        requestModelName: String
    ): String {
        val endpointHavePath = endpoint.contains("{")

        val pathKeys = endpoint
            .replace("/", "")
            .split("{")
            .filter { it.contains("}") }
            .map {
                it.replace("}", "")
            }

        val pathParams = pathKeys.map {
            "$it: String"
        }.fold("") { acc, v ->
            "$acc\t$v,\n"
        }

        val finalEndpoint = if (endpointHavePath) {
            endpoint.replace("{", "$" + "{")
        } else {
            endpoint
        }

        val pt1 =
            """
                suspend fun $functionName(
                    httpClient: HttpClient,
            """
        val pt2 = if (endpointHavePath) {
            pathParams
        } else {
            ""
        }
        val pt3 =
            """
                request: $requestModelName
                ): Result<$responseModelName> = withContext(Dispatchers.IO) {
                    kotlin.runCatching {

                        val response = httpClient
                            .${method.lowercase()}("$finalEndpoint") {
                                url {
                                    Json.encodeToJsonElement(request).jsonObject
                                        .forEach {
                                            parameters.append(it.key, it.value.toString())
                                        }
                                }
                            }

                        response
                            .bodyAsText()
                            .let {
                                Json.decodeFromString<$responseModelName>(it)
                            }
                    }
                }
            """.trimIndent()

        return "$pt1$pt2$pt3"
    }
}
