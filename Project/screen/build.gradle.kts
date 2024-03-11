/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    id("LibraryConventionV1")
    id("FeatureJetpackCompose")
    id("FeatureContextReceiver")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.singularityindonesia.screen"
}

dependencies {

    implementation(project(":Library:webrepository"))
    implementation(project(":Library:model"))
    implementation(project(":Library:data"))
    implementation(project(":Library:exception"))
    implementation(project(":Library:serialization"))

    implementation(project(":Project:library:main-context"))
    implementation(project(":Project:library:analytic"))
    implementation(project(":Project:library:flow"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    // orbit mvi
//    implementation(libs.orbit.core)
//    implementation(libs.orbit.viewmodel)
//    implementation(libs.orbit.compose)
//    testImplementation(libs.orbit.test)

    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}