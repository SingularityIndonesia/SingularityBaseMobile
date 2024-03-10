/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention

import com.android.build.api.dsl.LibraryExtension
import com.singularityindonesia.convention.companion.DefaultConfigs.COMPILE_SDK
import com.singularityindonesia.convention.companion.DefaultConfigs.JAVA_SOURCE_COMPAT
import com.singularityindonesia.convention.companion.DefaultConfigs.JAVA_TARGET_COMPAT
import com.singularityindonesia.convention.companion.DefaultConfigs.JVM_TARGET
import com.singularityindonesia.convention.companion.DefaultConfigs.MIN_SDK
import com.singularityindonesia.convention.companion.DefaultConfigs.PROGUARD_ANDROID_OPTIMIZE
import com.singularityindonesia.convention.companion.DefaultConfigs.PROGUARD_CONSUMER_FILES
import com.singularityindonesia.convention.companion.DefaultConfigs.PROGUARD_RULES
import com.singularityindonesia.convention.companion.DefaultConfigs.TEST_INST_RUNNER
import com.singularityindonesia.convention.companion.getSigningConfig
import com.singularityindonesia.convention.companion.kotlinCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.*

open class LibraryConventionV1 : Plugin<Project> {

    private val PLUGINS = listOf(
        "com.android.library",
        "org.jetbrains.kotlin.android"
    )

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                PLUGINS.forEach(::apply)
            }

            extensions.configure<LibraryExtension> {
                compileSdk = COMPILE_SDK
                defaultConfig {
                    minSdk = MIN_SDK
                    testInstrumentationRunner = TEST_INST_RUNNER
                    consumerProguardFiles(
                        *PROGUARD_CONSUMER_FILES
                    )
                }
                buildTypes {

                    val signingConfigAll = getSigningConfig(
                        project = project,
                        extension = this@configure
                    )

                    release {
                        if (signingConfigAll != null)
                            this.signingConfig = signingConfigAll

                        isMinifyEnabled = true
                        isJniDebuggable = false
                        proguardFiles(
                            getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE
                            ),
                            PROGUARD_RULES
                        )
                    }

                    debug {
                        if (signingConfigAll != null)
                            this.signingConfig = signingConfigAll

                        isMinifyEnabled = false
                        isJniDebuggable = true
                        proguardFiles(
                            getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE
                            ),
                            PROGUARD_RULES
                        )
                    }
                    all {
                        if (name.lowercase().endsWith("debug")) {
                            setMatchingFallbacks("debug")
                        } else {
                            setMatchingFallbacks("release")
                        }
                    }
                }
                compileOptions {
                    sourceCompatibility = JAVA_SOURCE_COMPAT
                    targetCompatibility = JAVA_TARGET_COMPAT
                }

                kotlinCompile {
                    kotlinOptions {
                        jvmTarget = JVM_TARGET
                    }
                }
            }
        }
    }
}