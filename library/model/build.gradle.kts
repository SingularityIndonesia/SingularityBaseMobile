/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
}
