/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 17/05/2024 14:05
 * You are not allowed to remove the copyright.
 */
package plugin.postman_client_generator.companion

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.addToSourceSet(
    path: String
) {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.commonMain {
            kotlin.srcDir(path)
        }
    }
}