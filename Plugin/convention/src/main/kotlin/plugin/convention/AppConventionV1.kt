/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention

import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import plugin.convention.companion.DefaultConfigs.EXCLUDED_RESOURCES
import plugin.convention.companion.versionCatalog
import plugin.convention.companion.withBaseAppModuleExtension
import plugin.convention.companion.withKotlinMultiplatformExtension
import plugin.convention.companion.withPluginManager
import java.io.FileInputStream
import java.util.Properties

class AppConventionV1 : Plugin<Project> {
    companion object {
        public val ID: String = "AppConventionV1"
    }

    override fun apply(project: Project) =
        with(project) {
            val libs = versionCatalog

            withPluginManager {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.multiplatform")
            }

            withKotlinMultiplatformExtension {
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

                sourceSets.commonTest.dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("junit").get())
                }
                sourceSets.androidNativeTest.dependencies {
                    implementation(libs.findLibrary("androidx-test-junit").get())
                    implementation(libs.findLibrary("androidx-espresso-core").get())
                }
            }
            withBaseAppModuleExtension {
                compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()

                sourceSets["main"].manifest.srcFile("android/AndroidManifest.xml")
                sourceSets["main"].res.srcDirs("android/res")
                sourceSets["main"].resources.srcDirs("common/res")

                defaultConfig {
                    minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
                    targetSdk = libs.findVersion("android-targetSdk").get().toString().toInt()
                }

                // signing config
                val signingConfig = generateSigningConfig(project, this)

                // You only need to define build variant once here.
                // No need to define it in every module, because only composeApp module can have contexts,
                // and only composeApp module can access the environment variables.
                // Other modules are prohibited.
                defineBuildVariants(
                    mod = this,
                    signingConfig = signingConfig
                )

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

    fun generateSigningConfig(
        project: Project,
        mod: BaseAppModuleExtension
    ) = with(mod) {
        val keystore = run {
            Properties()
                .apply {
                    load(
                        FileInputStream(
                            project.file("${project.projectDir}/keystore.properties")
                        )
                    )
                }
        }

        signingConfigs.create("all") {
            storeFile(project.file(keystore.getProperty("store.file")))
            storePassword = keystore.getProperty("store.password")
            keyAlias = keystore.getProperty("store.key.alias")
            keyPassword = keystore.getProperty("store.key.password")
        }
    }

    fun defineBuildVariants(
        mod: BaseAppModuleExtension,
        signingConfig: ApkSigningConfig?
    ) = with(mod) {
        // put your environment variable in environment.properties file within this composeApp project dir

        buildFeatures {
            buildConfig = true
        }

        buildTypes {
            release {
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                isMinifyEnabled = true
                this.signingConfig = signingConfig
            }

            debug {
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                isMinifyEnabled = false
                this.signingConfig = signingConfig
            }
        }
    }
}