/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.convention.companion.DefaultConfigs.EXCLUDED_RESOURCES

class LibraryConventionV2 : Plugin<Project> {

    companion object {
        public val ID: String = "LibraryConventionV1"
    }

    private val PLUGINS = listOf(
        "com.android.library",
        "org.jetbrains.kotlin.multiplatform",
    )

    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                PLUGINS.forEach(::apply)
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = "17"
                        }
                    }
                }

                sourceSets.commonMain {
                    kotlin.srcDir("common")
                    resources.srcDirs("common/res")
                }
                sourceSets.commonTest {
                    kotlin.srcDirs("commonTest")
                }
                sourceSets.androidMain {
                    kotlin.srcDir("android")
                }
                sourceSets.androidNativeTest {
                    kotlin.srcDir("androidTest")
                }
                sourceSets.iosMain {
                    kotlin.srcDir("ios")
                }
                sourceSets.iosTest {
                    kotlin.srcDirs("iosTest")
                }

                sourceSets.commonTest.dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("junit").get())
                }
                sourceSets.androidNativeTest.dependencies {
                    implementation(libs.findLibrary("androidx-test-junit").get())
                    implementation(libs.findLibrary("androidx-espresso-core").get())
                }
            }

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()

                sourceSets["main"].manifest.srcFile("android/AndroidManifest.xml")
                sourceSets["main"].res.srcDirs("android/res")
                sourceSets["main"].resources.srcDirs("common/res")

                defaultConfig {
                    minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
                    targetSdk = libs.findVersion("android-targetSdk").get().toString().toInt()
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
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }

}