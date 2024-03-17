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

    implementation("webrepository:main-SNAPSHOT")
    implementation("main-context:main-SNAPSHOT")

    implementation(libs.core.ktx)

    debugImplementation (libs.pluto)
    releaseImplementation (libs.pluto.no.op)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.cio)

    debugImplementation(libs.pluto.plugins.bundle.core)
    releaseImplementation(libs.pluto.plugins.bundle.core.no.op)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}