/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withKotlinMultiplatformExtension

class FeatureHttpClient : Plugin<Project> {
    companion object {
        public val ID: String = "FeatureHttpClient"
    }

    override fun apply(project: Project) {
        with(project) {

            val libs = versionCatalog

            withKotlinMultiplatformExtension {
                sourceSets.androidMain.dependencies {
                    implementation(libs.findLibrary("ktor-client-okhttp").get())
                    implementation(libs.findLibrary("ktor-client-core").get())
                }

                sourceSets.commonMain.dependencies {

                }

                sourceSets.iosMain.dependencies {
                    implementation(libs.findLibrary("ktor-client-ios").get())
                    implementation(libs.findLibrary("ktor-client-core").get())
                }
            }
        }
    }

}