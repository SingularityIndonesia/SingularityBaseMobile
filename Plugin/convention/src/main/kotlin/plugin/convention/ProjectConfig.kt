package plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.File

class ProjectConfig : Plugin<Project> {
    companion object {
        const val PROJECT_CONFIG_FILE = "config.properties"
        const val OUTPUT_PATH = "build/generated/project-config/ProjectConfig.kt"
    }

    override fun apply(
        target: Project
    ) {
        val file = target.file(PROJECT_CONFIG_FILE)
        val properties = Properties()
            .apply {
                load(file.inputStream())
                file.inputStream().close()
            }

        val kotlinClass = createDocument(properties)
        val outputFile = printDocument(target,kotlinClass)

        target.extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain {
                kotlin.srcDir(outputFile.parentFile.path)
            }
        }
    }

    private fun createDocument(
        properties: Properties
    ): String {
        val fields = properties.map {
            "const val ${it.key} = ${it.value}"
        }
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
    ) : File {
        val file = File(project.projectDir, OUTPUT_PATH)

        if (!file.exists())
            file.parentFile.mkdirs()

        println("print to file ${file.path}")
        file.writeText(string)

        return file
    }
}