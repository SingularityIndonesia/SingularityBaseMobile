/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.jvm.optionals.getOrElse

class FeatureHttpClient : Plugin<Project> {
    companion object {
        public val ID: String = "FeatureHttpClient"
    }

    override fun apply(target: Project) {
        with(target) {

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets.androidMain.dependencies {
                    implementation(
                        libs.findLibrary("ktor-client-okhttp").get()
                    )
                }

                sourceSets.commonMain.dependencies {
                    implementation(
                        libs.findLibrary("ktor-client-core").get()
                    )
                }

                sourceSets.iosMain.dependencies {
                    implementation(
                        libs.findLibrary("ktor-client-ios").get()
                    )
                }
            }
        }
    }

}