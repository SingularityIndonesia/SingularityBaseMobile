package plugin.convention.features

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.convention.companion.debugAllImplementation
import plugin.convention.companion.debugImplementation
import plugin.convention.companion.releaseAllImplementation
import plugin.convention.companion.releaseImplementation

class FeatureAndroidPluto : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                dependencies {
                    releaseImplementation(libs.findBundle("pluto-no-op").get())
                    debugImplementation(libs.findBundle("pluto").get())
                }
            }

            // in case of app project
            runCatching {
                extensions.configure<BaseAppModuleExtension> {
                    dependencies {
                        releaseAllImplementation(libs.findBundle("pluto-no-op").get())
                        debugAllImplementation(libs.findBundle("pluto").get())
                    }
                }
            }
        }

    }
}