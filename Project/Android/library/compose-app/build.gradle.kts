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

    implementation("std:webrepository")
    implementation("std:exception")
    implementation("std:analytic")
    implementation(project(":Project:Android:library:screen"))
    implementation(project(":Project:Android:library:designsystem"))

//    implementation(project(":Project:Android:library:main-context"))
//    implementation(project(":Project:Android:library:dictionary"))
//    implementation(project(":Project:Android:library:debugger"))

    implementation(libs.android.coroutine)
    implementation(libs.android.core.ktx)
    implementation(libs.android.lifecycle.runtime.ktx)

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
