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

    implementation("compose-app:main-SNAPSHOT")
    implementation("webrepository:main-SNAPSHOT")
    implementation("exception:main-SNAPSHOT")
    implementation("dictionary:main-SNAPSHOT")
    implementation("main-context:main-SNAPSHOT")
    implementation(project(":Project:Android:library:debugger"))

    implementation(libs.android.coroutine)
    implementation(libs.android.core.ktx)
    implementation(libs.android.lifecycle.runtime.ktx)

    implementation(libs.android.activity.compose)
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.graphics)
    implementation(libs.android.compose.ui.tooling.preview)
    implementation(libs.android.compose.material3)

    implementation(libs.android.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.ext.junit)
    androidTestImplementation(libs.android.espresso.core)
    androidTestImplementation(libs.android.compose.ui.test.junit4)
    debugImplementation(libs.android.compose.ui.tooling)
    debugImplementation(libs.android.compose.ui.test.manifest)
}