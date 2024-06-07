/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.convention.companion.debugImplementation

class FeaturePane : ComposePlugin() {
    companion object {
        public val ID: String = "FeaturePane"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.androidMain.dependencies {
                    implementation(Dependencies(target).preview)
                    implementation(
                        libs.findLibrary("androidx-activity-compose").get()
                    )
                }

                sourceSets.commonMain.dependencies {
                    implementation(Dependencies(target).runtime)
                    implementation(Dependencies(target).foundation)
                    implementation(Dependencies(target).material3)
                    implementation(Dependencies(target).ui)
                    implementation(Dependencies(target).components.resources)
                    implementation(Dependencies(target).components.uiToolingPreview)
                    implementation(libs.findLibrary("lifecycle-viewmodel-compose").get())
                }
            }

            val androidTestImplementation: DependencyHandler.(Any) -> Unit =
                { dependencyNotation ->
                    add("androidTestImplementation", dependencyNotation)
                }

            extensions.configure<BaseExtension> {
                dependencies {
                    debugImplementation(libs.findLibrary("compose-ui-tooling").get())
                    androidTestImplementation(
                        libs.findLibrary("compose-ui-tooling-preview").get()
                    )
                }
            }

            runCatching {
                extensions.configure<BaseAppModuleExtension> {
                    buildFeatures {
                        compose = true
                    }
                }
            }

            runCatching {
                extensions.configure<LibraryExtension> {
                    buildFeatures {
                        compose = true
                    }
                }
            }
        }
    }

}