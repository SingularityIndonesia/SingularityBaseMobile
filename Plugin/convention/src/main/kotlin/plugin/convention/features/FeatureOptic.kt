package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.companion.requirePlugin
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withKotlinMultiplatformExtension
import plugin.convention.companion.withPluginManager

class FeatureOptic : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.versionCatalog
        target.withPluginManager {
            requirePlugin("com.google.devtools.ksp") { apply(it) }
        }

        target.withKotlinMultiplatformExtension {
            sourceSets.commonMain.dependencies {
                implementation(libs.findLibrary("arrow-core").get())
                implementation(libs.findLibrary("arrow-optics").get())
            }
        }
    }
}
