/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package shared.common

interface Platform {
    val name: String
    fun isAndroid() = name.contains("android", true)
    fun isIOS() = name.contains("ios", true)
}

expect fun getPlatform(): Platform