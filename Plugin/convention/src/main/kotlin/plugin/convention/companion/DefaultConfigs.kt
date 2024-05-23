/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package plugin.convention.companion

// inprogress: move value to buildconfig.
object DefaultConfigs {

    // PROGUARD
    val TEST_INST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    val PROGUARD_CONSUMER_FILES = arrayOf<Any>(
        "consumer-rules.pro"
    )
    val PROGUARD_ANDROID_OPTIMIZE = "proguard-android-optimize.txt"
    val PROGUARD_RULES = "proguard-rules.pro"
    val EXCLUDED_RESOURCES = "/META-INF/{AL2.0,LGPL2.1}"

}