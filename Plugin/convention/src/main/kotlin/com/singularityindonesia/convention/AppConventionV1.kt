/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention

import com.android.build.api.dsl.ApplicationExtension
import com.singularityindonesia.convention.companion.DefaultConfigs
import com.singularityindonesia.convention.companion.DefaultConfigs.COMPILE_SDK
import com.singularityindonesia.convention.companion.DefaultConfigs.MIN_SDK
import com.singularityindonesia.convention.companion.DefaultConfigs.TARGET_SDK
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AppConventionV1 : Plugin<Project> {

    private val PLUGINS = listOf(
        "com.android.application",
        "org.jetbrains.kotlin.multiplatform"
    )

    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                PLUGINS.forEach(::apply)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = "11"
                        }
                    }
                }

                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

            }
            extensions.configure<ApplicationExtension> {
                compileSdk = COMPILE_SDK

                sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
                sourceSets["main"].res.srcDirs("src/androidMain/res")
                sourceSets["main"].resources.srcDirs("src/commonMain/resources")

                defaultConfig {
                    minSdk = MIN_SDK
                    targetSdk = TARGET_SDK
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }
    
}