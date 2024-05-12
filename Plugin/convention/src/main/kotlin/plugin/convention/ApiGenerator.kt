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
                    projectDir,
                    file,
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
        projectDir: File,
        contractDir: File,
        json: JsonObject,
    ) {
        generateResponseModel(
            projectDir,
            json
        )
    }

    private fun generateResponseModel(
        outputDir: File,
        json: JsonObject
    ) {
        val context = json["context"].toString().replace("\"", "")
        val methods = json["methods"]?.jsonArray ?: return // no method found

        val responseModels = methods.map {
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
    private val knownTypes = listOf("String", "Int", "Boolean", "Float", "Double")
    private fun toResponseDataClass(
        objectName: String,
        schema: JsonObject
    ): String {

        println("eadsa Eval : $objectName $schema")

        // evaluation
        val properties = schema.keys.toList()
            .filter { !knownSystemKeys.contains(it) }

        val propertyTypes = properties
            .map { prop ->
                val type = schema[prop]?.jsonObject?.get("type")
                    .toString()
                    .replace("\"", "")

                if (type == "object")
                    prop.replaceFirstChar { it.toString().uppercase() }
                else
                    type
            }
            .map {
                toActualType(it)
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
                val obj = schema[it.first]?.jsonObject
                    ?.get("value")?.jsonObject
                    ?: throw NullPointerException("data for ${it.first} is null")

                toResponseDataClass(
                    it.second,
                    obj
                )
            }
            .fold("") { acc, v ->
                "$acc\n$v"
            }

        if (subObject.isNotEmpty())
            dataClass =
                """
                    $dataClass{
                        $subObject
                    }
                """.trimIndent()

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
}
