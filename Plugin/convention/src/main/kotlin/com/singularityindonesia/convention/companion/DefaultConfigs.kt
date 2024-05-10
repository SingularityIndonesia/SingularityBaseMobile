/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.convention.companion

import org.gradle.api.JavaVersion

object DefaultConfigs {
    val TARGET_SDK = 34
    val COMPILE_SDK = 34
    val MIN_SDK = 29
    val TEST_INST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    val PROGUARD_CONSUMER_FILES = arrayOf<Any>(
        "consumer-rules.pro"
    )
    val PROGUARD_ANDROID_OPTIMIZE = "proguard-android-optimize.txt"
    val PROGUARD_RULES = "proguard-rules.pro"
    val JAVA_SOURCE_COMPAT = JavaVersion.VERSION_17
    val JAVA_TARGET_COMPAT = JavaVersion.VERSION_17
    val JVM_TARGET = "17"
    val EXCLUDED_RESOURCES = "/META-INF/{AL2.0,LGPL2.1}"
    val KOTLIN_COMPILER_EXTENSION_VERSION = "1.5.10"
}