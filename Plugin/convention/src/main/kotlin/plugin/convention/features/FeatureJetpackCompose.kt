/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import VersionCatalog.JVM_TARGET
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureJetpackCompose : ComposePlugin() {
    companion object {
        public val ID: String = "FeatureJetpackCompose"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

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
                    implementation("androidx.activity:activity-compose:1.9.0")
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