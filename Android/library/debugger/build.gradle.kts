/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    id("LibraryConventionV1")
}

android {
    namespace = "com.singularityindonesia.debugger"
}

dependencies {

    implementation("shared:webrepository")
    implementation("shared:main-context")

    implementation(libs.android.core.ktx)

    debugImplementation (libs.android.pluto)
    releaseImplementation (libs.android.pluto.no.op)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.cio)

    debugImplementation(libs.android.pluto.plugins.bundle.core)
    releaseImplementation(libs.android.pluto.plugins.bundle.core.no.op)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.ext.junit)
    androidTestImplementation(libs.android.espresso.core)
}