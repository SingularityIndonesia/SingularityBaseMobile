/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withKotlinMultiplatformExtension

class FeatureCoroutine : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {

            val libs = versionCatalog

            withKotlinMultiplatformExtension {

                sourceSets.androidMain.dependencies {
                    implementation(libs.findLibrary("coroutine-android").get())
                }

                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("coroutine-core").get())
                }

                sourceSets.iosMain.dependencies {

                }

                sourceSets["iosArm64Main"].dependencies {
                    implementation(libs.findLibrary("coroutine-core-iosarm64").get())
                }

                sourceSets["iosSimulatorArm64Main"].dependencies {
                    implementation(libs.findLibrary("coroutine-core-iossimulatorarm64").get())
                }

                sourceSets.jsMain.dependencies {
                    implementation(libs.findLibrary("coroutine-core-js").get())
                }
            }
        }
    }

}