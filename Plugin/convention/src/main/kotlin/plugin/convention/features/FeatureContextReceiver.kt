/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import plugin.convention.companion.kotlinCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureContextReceiver : Plugin<Project> {

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