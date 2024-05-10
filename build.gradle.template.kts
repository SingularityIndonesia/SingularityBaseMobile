plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    // issue in version catalogue
    /*alias(libs.plugins.kotlinxSerialization) apply false*/
    id("org.jetbrains.kotlin.plugin.serialization") version (libs.versions.kotlin.get()) apply false
}