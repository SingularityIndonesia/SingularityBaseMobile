package plugin.convention.companion

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties

val Project.environmentVariables
    get() = run {
        Properties()
            .apply {
                load(
                    FileInputStream(
                        project.file("${project.projectDir}/config.properties")
                    )
                )
            }
    }

fun Project.cfg(propertyName: String) : String  {
    return environmentVariables.getProperty(propertyName)
}