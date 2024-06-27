package plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties
import plugin.convention.companion.withKotlinMultiplatformExtension
import java.io.File

class ProjectConfig : Plugin<Project> {
    companion object {
        const val PROJECT_CONFIG_FILE = "config.properties"
        const val OUTPUT_PATH = "build/generated/project-config/ProjectConfig.kt"
    }

    override fun apply(project: Project) {
        with(project) {
            val file = file(PROJECT_CONFIG_FILE)
            val properties = Properties()
                .apply {
                    load(file.inputStream())
                    file.inputStream().close()
                }

            val kotlinClass = createDocument(properties)
            val outputFile = printDocument(this, kotlinClass)

            withKotlinMultiplatformExtension {
                sourceSets.commonMain {
                    kotlin.srcDir(outputFile.parentFile.path)
                }
            }
        }
    }

    private fun createDocument(properties: Properties): String {
        val fields = properties.map { "const val ${it.key} = ${it.value}" }
        val document =
            """
                object ProjectConfig {
                    ${fields.joinToString("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t")}
                }
            """.trimIndent()

        return document
    }

    private fun printDocument(
        project: Project,
        string: String
    ): File {
        val file = File(project.projectDir, OUTPUT_PATH)

        if (!file.exists())
            file.parentFile.mkdirs()

        file.writeText(string)
        return file
    }
}