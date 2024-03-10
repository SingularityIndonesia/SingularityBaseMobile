/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    id("LibraryConventionV1")
}

android {
    namespace = "com.singularityindonesia.analytics"
}

dependencies {

    implementation(libs.core.ktx)
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation(project(":Project:library:exception"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}