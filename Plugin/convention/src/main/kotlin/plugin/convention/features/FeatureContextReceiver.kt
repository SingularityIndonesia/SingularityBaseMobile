/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.convention.companion.kotlinCompile

class FeatureContextReceiver : Plugin<Project> {
    companion object {
        public val ID: String = "FeatureContextReceiver"
    }

    override fun apply(target: Project) {
        target.extensions.configure<KotlinMultiplatformExtension> {
            target.kotlinCompile {
                kotlinOptions {
                    freeCompilerArgs += "-Xcontext-receivers"
                }
            }
        }
    }

}