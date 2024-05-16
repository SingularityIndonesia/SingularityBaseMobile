package plugin.postman_client_generator.companion

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.addToSourceSet(
    path: String
) {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.commonMain {
            kotlin.srcDir(path)
        }
    }
}