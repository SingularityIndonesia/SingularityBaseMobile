/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention

import com.android.build.api.dsl.ApplicationExtension
import com.singularityindonesia.convention.companion.DefaultConfigs.COMPILE_SDK
import com.singularityindonesia.convention.companion.DefaultConfigs.EXCLUDED_RESOURCES
import com.singularityindonesia.convention.companion.DefaultConfigs.JAVA_SOURCE_COMPAT
import com.singularityindonesia.convention.companion.DefaultConfigs.JAVA_TARGET_COMPAT
import com.singularityindonesia.convention.companion.DefaultConfigs.JVM_TARGET
import com.singularityindonesia.convention.companion.DefaultConfigs.MIN_SDK
import com.singularityindonesia.convention.companion.DefaultConfigs.PROGUARD_ANDROID_OPTIMIZE
import com.singularityindonesia.convention.companion.DefaultConfigs.PROGUARD_RULES
import com.singularityindonesia.convention.companion.DefaultConfigs.TEST_INST_RUNNER
import com.singularityindonesia.convention.companion.getSigningConfig
import com.singularityindonesia.convention.companion.kotlinCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AppConventionV1 : Plugin<Project> {

    private val PLUGINS = listOf(
        "org.jetbrains.kotlin.android",
        "com.android.application"
    )

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                PLUGINS.forEach(::apply)
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = COMPILE_SDK
                defaultConfig {
                    minSdk = MIN_SDK
                    testInstrumentationRunner = TEST_INST_RUNNER
                    vectorDrawables {
                        useSupportLibrary = true
                    }
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
                        isDebuggable = false
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
                        isDebuggable = true
                        isJniDebuggable = true
                        proguardFiles(
                            getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE
                            ),
                            PROGUARD_RULES
                        )
                    }
                    create("devDebug") {
                        initWith(getByName("debug"))
                        isDefault = true
                    }
                    create("dev") {
                        initWith(getByName("release"))
                    }
                    create("stagingDebug") {
                        initWith(getByName("debug"))
                    }
                    create("staging") {
                        initWith(getByName("release"))
                    }
                    create("prodDebug") {
                        initWith(getByName("debug"))
                    }
                    create("prod") {
                        initWith(getByName("release"))
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

                packaging {
                    resources {
                        excludes += EXCLUDED_RESOURCES
                    }
                }
            }
        }
    }
}