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
                val objectName = it["method"]
                    .toString()
                    .replace("\"", "")
                    .let { method ->
                        "$context${method}Response"
                    }

                val fileContent = it["response"]
                    ?.jsonObject
                    ?.let {
                        toResponseDataClass(
                            objectName,
                            it
                        )
                    }
                    ?: "ERROR EMPTY"

                val fileName = "$objectName.kt"

                fileName to fileContent
            }

            // attach package name
            .map {
                val newValue = """
                    package $namespace.model
                    
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

                when(type) {
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
            val pt1 = "data class $objectName("
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
                    .replace("List<","")
                    .replace(">","")

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
                val responseModel = "$context${method.uppercase()}Response"

                generateApiClient(
                    functionName,
                    method,
                    endpoint,
                    responseModel
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
                        "import $namespace.model.${context}${method}Response"
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
                """.trimIndent()

                val pt2 = modelDependencies
                val pt3 = it

                "$pt1$pt2$pt3"
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
        responseModel: String
    ): String {
        val client =
            """
                suspend fun $functionName(
                    httpClient: HttpClient
                ): Result<$responseModel> = withContext(Dispatchers.IO) {
                    kotlin.runCatching {

                        val response = httpClient
                            .${method.lowercase()}("$endpoint")

                        response
                            .bodyAsText()
                            .let {
                                Json.decodeFromString<$responseModel>(it)
                            }
                    }
                }
            """.trimIndent()

        return client
    }
}
