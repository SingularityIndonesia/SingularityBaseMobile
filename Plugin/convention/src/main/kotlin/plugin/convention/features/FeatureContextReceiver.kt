/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.convention.companion.kotlinCompile
import plugin.convention.companion.withKotlinMultiplatformExtension

class FeatureContextReceiver : Plugin<Project> {
    companion object {
        public val ID: String = "FeatureContextReceiver"
    }

    override fun apply(project: Project) {
        with(project) {
            withKotlinMultiplatformExtension {
                kotlinCompile {
                    kotlinOptions {
                        freeCompilerArgs += "-Xcontext-receivers"
                    }
                }
            }
        }
    }

}