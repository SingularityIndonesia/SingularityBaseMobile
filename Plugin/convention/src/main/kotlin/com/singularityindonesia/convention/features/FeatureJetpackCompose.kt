/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention.features

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.singularityindonesia.convention.companion.DefaultConfigs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class FeatureJetpackCompose : Plugin<Project> {

    private val KOTLIN_COMPILER_EXTENSION_VERSION = DefaultConfigs.KOTLIN_COMPILER_EXTENSION_VERSION

    override fun apply(target: Project) {
        runCatching {
            setupLibrary(target)
        }
        runCatching {
            setupApplication(target)
        }
    }

    private fun setupLibrary(target: Project) =
        target.extensions.configure<LibraryExtension> {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = KOTLIN_COMPILER_EXTENSION_VERSION
            }
        }

    private fun setupApplication(target: Project) =
        target.extensions.configure<ApplicationExtension> {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = KOTLIN_COMPILER_EXTENSION_VERSION
            }
        }

}