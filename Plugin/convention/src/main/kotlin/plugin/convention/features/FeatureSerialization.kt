/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withKotlinMultiplatformExtension
import plugin.convention.companion.withPluginManager

class FeatureSerialization : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            val libs = versionCatalog

            withPluginManager {
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            withKotlinMultiplatformExtension {
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("kotlinx-serialization-json").get())
                }
            }
        }
    }
}