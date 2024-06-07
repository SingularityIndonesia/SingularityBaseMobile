/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureCoroutine : Plugin<Project> {
    companion object {
        public val ID: String = "FeatureCoroutine"
    }

    override fun apply(target: Project) {
        with(target) {

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets.androidMain.dependencies {
                    implementation(
                        libs.findLibrary("coroutine-android").get()
                    )
                }

                sourceSets.commonMain.dependencies {
                    implementation(
                        libs.findLibrary("coroutine-core").get()
                    )
                }

                sourceSets.iosMain.dependencies {

                }

                sourceSets["iosArm64Main"].dependencies {
                    implementation(
                        libs.findLibrary("coroutine-core-iosarm64").get()
                    )
                }

                sourceSets["iosSimulatorArm64Main"].dependencies {
                    implementation(
                        libs.findLibrary("coroutine-core-iossimulatorarm64").get()
                    )
                }

                sourceSets.jsMain.dependencies {
                    implementation(
                        libs.findLibrary("coroutine-core-js").get()
                    )
                }
            }
        }
    }

}