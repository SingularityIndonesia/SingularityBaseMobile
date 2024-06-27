/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ComposePlugin
import plugin.convention.companion.androidTestImplementation
import plugin.convention.companion.debugImplementation
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withBaseAppModuleExtension
import plugin.convention.companion.withBaseExtension
import plugin.convention.companion.withKotlinMultiplatformExtension
import plugin.convention.companion.withLibraryExtension
import plugin.convention.companion.withPluginManager

class FeaturePane : ComposePlugin() {

    override fun apply(project: Project) {
        with(project) {
            val libs = versionCatalog

            withPluginManager {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            withKotlinMultiplatformExtension {
                sourceSets.androidMain.dependencies {
                    implementation(Dependencies(project).preview)
                    implementation(
                        libs.findLibrary("androidx-activity-compose").get()
                    )
                }

                sourceSets.commonMain.dependencies {
                    implementation(Dependencies(project).runtime)
                    implementation(Dependencies(project).foundation)
                    implementation(Dependencies(project).material3)
                    implementation(Dependencies(project).ui)
                    implementation(Dependencies(project).components.resources)
                    implementation(Dependencies(project).components.uiToolingPreview)
                    implementation(libs.findLibrary("compose-navigation").get())
                    implementation(libs.findLibrary("lifecycle-viewmodel-compose").get())
                }
            }

            withBaseExtension {
                dependencies {
                    debugImplementation(libs.findLibrary("compose-ui-tooling").get())
                    androidTestImplementation(
                        libs.findLibrary("compose-ui-tooling-preview").get()
                    )
                }
            }

            // configure for application module
            runCatching {
                withBaseAppModuleExtension {
                    buildFeatures {
                        compose = true
                    }
                }
            }

            // configure for library module
            runCatching {
                withLibraryExtension {
                    buildFeatures {
                        compose = true
                    }
                }
            }
        }
    }
}