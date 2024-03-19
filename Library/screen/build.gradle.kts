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

    implementation(project(":webrepository"))
    implementation(project(":model"))
    implementation(project(":data"))
    implementation(project(":exception"))
    implementation(project(":serialization"))
    implementation(project(":analytic"))
    implementation(project(":main-context"))
    implementation(project(":flow"))

    implementation(libs.android.core.ktx)
    implementation(libs.android.appcompat)
    implementation(libs.android.material)

    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.graphics)
    implementation(libs.android.compose.ui.tooling.preview)
    implementation(libs.android.compose.material3)

    implementation(libs.android.lifecycle.viewmodel.compose)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.ext.junit)
    androidTestImplementation(libs.android.espresso.core)
}