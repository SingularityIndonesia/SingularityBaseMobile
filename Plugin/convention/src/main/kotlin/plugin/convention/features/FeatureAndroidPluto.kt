package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import plugin.convention.companion.debugAllImplementation
import plugin.convention.companion.debugImplementation
import plugin.convention.companion.releaseAllImplementation
import plugin.convention.companion.releaseImplementation
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withBaseAppModuleExtension
import plugin.convention.companion.withKotlinMultiplatformExtension

class FeatureAndroidPluto : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {
            val libs = versionCatalog

            withKotlinMultiplatformExtension {
                dependencies {
                    releaseImplementation(libs.findBundle("pluto-no-op").get())
                    debugImplementation(libs.findBundle("pluto").get())
                }
            }

            // in case of app project
            runCatching {
                withBaseAppModuleExtension {
                    dependencies {
                        releaseAllImplementation(libs.findBundle("pluto-no-op").get())
                        debugAllImplementation(libs.findBundle("pluto").get())
                    }
                }
            }
        }

    }
}