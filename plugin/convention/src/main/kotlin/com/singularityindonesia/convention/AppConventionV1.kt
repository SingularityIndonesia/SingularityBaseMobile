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
import com.singularityindonesia.convention.companion.KEYSTORE_PROPERTIES_FILE_NAME
import com.singularityindonesia.convention.companion.getSigningConfig
import com.singularityindonesia.convention.companion.kotlinCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.*

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
                        proguardFiles(
                            getDefaultProguardFile(
                                PROGUARD_ANDROID_OPTIMIZE
                            ),
                            PROGUARD_RULES
                        )
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