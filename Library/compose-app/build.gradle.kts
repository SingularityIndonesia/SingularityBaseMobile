/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    id("LibraryConventionV1")
    id("FeatureJetpackCompose")
    id("FeatureContextReceiver")
}

android {
    namespace = "com.singularityindonesia.composeapp"
}

dependencies {

    implementation(project(":webrepository"))
    implementation(project(":exception"))
    implementation(project(":analytic"))
    implementation(project(":screen"))
    implementation(project(":designsystem"))

//    implementation(project(":Project:Android:library:main-context"))
//    implementation(project(":Project:Android:library:dictionary"))
//    implementation(project(":Project:Android:library:debugger"))

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
