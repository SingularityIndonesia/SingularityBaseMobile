/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention.features

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.singularityindonesia.convention.companion.kotlinCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class FeatureContextReceiver : Plugin<Project> {

    override fun apply(target: Project) {
        listOf(::setupLibrary, ::setupApplication)
            .map {
                runCatching {
                    setupLibrary(target)
                }
            }
            .also {
                if (it.count { r -> r.isSuccess } == 0 )
                    println("Error : Project is not android library or android application.")
            }
    }

    private fun setupLibrary(target: Project) =
        target.extensions.configure<LibraryExtension> {
            target.kotlinCompile {
                kotlinOptions {
                    freeCompilerArgs += "-Xcontext-receivers"
                }
            }
        }


    private fun setupApplication(target: Project) =
        target.extensions.configure<ApplicationExtension> {
            target.kotlinCompile {
                kotlinOptions {
                    freeCompilerArgs += "-Xcontext-receivers"
                }
            }
        }

}