package com.singularityindonesia.convention.companion

import org.gradle.api.JavaVersion

object DefaultConfigs {
    val COMPILE_SDK = 34
    val MIN_SDK = 28
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
}