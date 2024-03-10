/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    id("AppConventionV1")
    id("FeatureJetpackCompose")
    id("FeatureContextReceiver")
}

android {
    namespace = "com.singularityindonesia.singularityindonesia"
    defaultConfig {
        versionCode = 1
        versionName = "1.0.0"
    }
}

dependencies {

    implementation(project(":Project:library:designsystem"))
    implementation(project(":Project:library:webrepository"))
    implementation(project(":Project:library:main-context"))
    implementation(project(":Project:library:dictionary"))
    implementation(project(":Project:library:analytic"))
    implementation(project(":Project:library:debugger"))
    implementation(project(":Project:screen"))

    implementation(libs.coroutine.android)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    implementation(libs.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}