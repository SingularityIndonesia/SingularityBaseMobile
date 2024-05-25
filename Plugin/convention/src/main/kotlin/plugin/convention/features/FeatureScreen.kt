/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import VersionCatalog
import VersionCatalog.JVM_TARGET
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.jvm.optionals.getOrElse

class FeatureScreen : ComposePlugin() {
    companion object {
        public val ID: String = "FeatureScreen"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = JVM_TARGET
                        }
                    }
                }

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
                }
            }
        }
    }

}