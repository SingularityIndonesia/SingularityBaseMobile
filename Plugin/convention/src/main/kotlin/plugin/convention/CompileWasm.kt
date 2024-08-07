/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.convention.companion.withKotlinMultiplatformExtension

class CompileWasm : Plugin<Project> {
    override fun apply(target: Project) {
        val absolutePath = target.projectDir.path
        val name =
            target.path
                .replaceFirstChar { "" }
                .replace(":", "-")

        val _moduleName =
            when {
                absolutePath.contains("/Shared", true) -> "shared-$name"
                absolutePath.contains("/System", true) -> "system-$name"
                else -> name
            }

        with(target) {
            withKotlinMultiplatformExtension {
                wasmJs {
                    moduleName = _moduleName

                    browser {
                        commonWebpackConfig {
                            outputFileName = "$_moduleName.js"
                            devServer =
                                (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                                    static =
                                        (static ?: mutableListOf()).apply {
                                            // Serve sources to debug inside browser
                                            add(project.projectDir.path)
                                        }
                                }
                        }
                    }
                    binaries.executable()
                }
                sourceSets.wasmJsMain {
                    kotlin.srcDirs("web")
                    resources.srcDirs("web/res")
                }
            }
        }
    }
}
