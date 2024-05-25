/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.features

import VersionCatalog.JVM_TARGET
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureScreenNavigation : ComposePlugin() {
    companion object {
        public val ID: String = "FeatureJetpackCompose"
    }

    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets.androidMain.dependencies {
                    implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha03")
                }
            }
        }
    }

}