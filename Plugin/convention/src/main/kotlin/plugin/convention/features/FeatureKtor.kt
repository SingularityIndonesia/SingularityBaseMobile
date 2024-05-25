/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureKtor : Plugin<Project> {
    companion object {
        public val ID: String = "FeatureKtor"
    }
    override fun apply(target: Project) {
        with(target) {

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets.androidMain.dependencies {
                    implementation("io.ktor:ktor-client-okhttp:2.3.11")
                }

                sourceSets.commonMain.dependencies {
                    implementation("io.ktor:ktor-client-core:2.3.11")
                }

                sourceSets.iosMain.dependencies {
                    implementation("io.ktor:ktor-client-ios:2.3.11")
                }
            }
        }
    }

}