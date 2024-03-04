package com.singularityindonesia.convention.features

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.singularityindonesia.convention.companion.DefaultConfigs
import com.singularityindonesia.convention.companion.kotlinCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class FeatureContextReceiver : Plugin<Project> {

    override fun apply(target: Project) {
        kotlin.runCatching {
            setupLibrary(target)
        }
        kotlin.runCatching {
            setupApplication(target)
        }
    }

    private fun setupLibrary(target: Project) {
        target.extensions.configure<LibraryExtension> {
            target.kotlinCompile {
                kotlinOptions {
                    freeCompilerArgs += "-Xcontext-receivers"
                }
            }
        }
    }

    private fun setupApplication(target: Project) {
        target.extensions.configure<ApplicationExtension> {
            target.kotlinCompile {
                kotlinOptions {
                    freeCompilerArgs += "-Xcontext-receivers"
                }
            }
        }
    }
}