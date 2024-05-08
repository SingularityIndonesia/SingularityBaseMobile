/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    kotlin("jvm")
}

dependencies {

    implementation("shared:data")
    implementation("shared:exception")
    implementation(libs.coroutine)

    testImplementation(libs.junit)
}