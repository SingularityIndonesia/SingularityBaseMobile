/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CompileIOS : Plugin<Project> {
    companion object {
        public val ID: String = "CompileIOS"
    }

    override fun apply(target: Project) {
        val absolutePath = target.projectDir.path
        val name = target.path.replaceFirstChar { "" }
            .split(":")
            .joinToString("") { it.replaceFirstChar { it.uppercaseChar() } }
            .split("-")
            .joinToString("") { it.replaceFirstChar { it.uppercaseChar() } }
            .split("_")
            .joinToString("") { it.replaceFirstChar { it.uppercaseChar() } }

        val _moduleName = when {
            absolutePath.contains("/Shared", true) -> "Shared$name"
            absolutePath.contains("/System", true) -> "System$name"
            else -> name
        }

        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = _moduleName
                        isStatic = true
                        /*linkerOpts += "-ld64"*/
                    }
                }
                sourceSets.iosMain {
                    kotlin.srcDir("ios")
                    resources.srcDirs("ios/res")
                }
                sourceSets.iosTest {
                    kotlin.srcDirs("iosTest")
                    resources.srcDirs("ios/res")
                }
            }
        }
    }

}