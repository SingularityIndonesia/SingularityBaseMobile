/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package common

interface Platform {
    val name: String
}

fun Platform.isAndroid() = name.contains("android", true)
fun Platform.isIOS() = name.contains("ios", true)

fun Platform.isWeb() = name.contains("web", true)

expect fun getPlatform(): Platform