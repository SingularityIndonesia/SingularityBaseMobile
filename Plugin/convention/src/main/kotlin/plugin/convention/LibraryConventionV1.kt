/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package plugin.convention

import com.android.build.gradle.LibraryExtension
import VersionCatalog.COMPILE_SDK
import VersionCatalog.JAVA_SOURCE_COMPAT
import VersionCatalog.JAVA_TARGET_COMPAT
import VersionCatalog.JUNIT_VERSION
import VersionCatalog.JVM_TARGET
import VersionCatalog.KOTLIN_VERSION
import VersionCatalog.MIN_SDK
import VersionCatalog.TARGET_SDK
import plugin.convention.companion.DefaultConfigs.EXCLUDED_RESOURCES
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class LibraryConventionV1 : Plugin<Project> {

    private val PLUGINS = listOf(
        "com.android.library",
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
                            jvmTarget = JVM_TARGET
                        }
                    }
                }
                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                )

                sourceSets.commonTest.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test:$KOTLIN_VERSION")
                    implementation("org.jetbrains.kotlin:kotlin-test-junit:$KOTLIN_VERSION")
                    implementation("junit:junit:$JUNIT_VERSION")
                }
            }

            extensions.configure<LibraryExtension> {
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
                        excludes += EXCLUDED_RESOURCES
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
                compileOptions {
                    sourceCompatibility = JAVA_SOURCE_COMPAT
                    targetCompatibility = JAVA_TARGET_COMPAT
                }
            }
        }

}