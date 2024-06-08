package plugin.convention.features

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureAdaptiveLayout : Plugin<Project>{
    override fun apply(target: Project) {
        target.extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.androidMain.dependencies {
                implementation("androidx.compose.material3.adaptive:adaptive")
                implementation("androidx.compose.material3.adaptive:adaptive-layout")
                implementation("androidx.compose.material3.adaptive:adaptive-navigation")
            }
        }
    }
}