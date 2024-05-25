/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention

import VersionCatalog.COMPILE_SDK
import VersionCatalog.JAVA_SOURCE_COMPAT
import VersionCatalog.JAVA_TARGET_COMPAT
import VersionCatalog.JUNIT_VERSION
import VersionCatalog.JVM_TARGET
import VersionCatalog.MIN_SDK
import VersionCatalog.TARGET_SDK
import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.convention.companion.DefaultConfigs.EXCLUDED_RESOURCES
import java.io.FileInputStream
import java.util.Properties

class AppConventionV1 : Plugin<Project> {
    companion object {
        public val ID: String = "AppConventionV1"
    }

    private val PLUGINS = listOf(
        "com.android.application",
        "org.jetbrains.kotlin.multiplatform"
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
                            jvmTarget = JVM_TARGET
                        }
                    }
                }

                sourceSets.commonTest.dependencies {
                    implementation(libs.findLibrary("junit").get())
                }
            }
            extensions.configure<BaseAppModuleExtension> {
                compileSdk = COMPILE_SDK

                sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
                sourceSets["main"].res.srcDirs("src/androidMain/res")
                sourceSets["main"].resources.srcDirs("src/commonMain/resources")

                defaultConfig {
                    minSdk = MIN_SDK
                    targetSdk = TARGET_SDK
                }

                // signing config
                val signingConfig = generateSigningConfig(target, this)

                // You only need to define build variant once here.
                // No need to define it in every module, because only composeApp module can have contexts,
                // and only composeApp module can access the environment variables.
                // Other modules are prohibited.
                defineBuildVariants(
                    project = target,
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
                    sourceCompatibility = JAVA_SOURCE_COMPAT
                    targetCompatibility = JAVA_TARGET_COMPAT
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
        project: Project,
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

            create("devDebug") {
                initWith(getByName("debug"))
                matchingFallbacks.add("debug")
            }

            create("devRelease") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")
            }

            create("stagingDebug") {
                initWith(getByName("debug"))
                matchingFallbacks.add("debug")
            }

            create("stagingRelease") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")
            }

            create("prodDebug") {
                initWith(getByName("debug"))
                matchingFallbacks.add("debug")
            }

            create("prodRelease") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")
            }
        }
    }
}