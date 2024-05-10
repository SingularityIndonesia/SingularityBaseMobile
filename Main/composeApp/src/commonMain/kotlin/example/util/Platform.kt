package example.util

interface Platform {
    val name: String
    fun isAndroid() = name.contains("android", true)
    fun isIOS() = name.contains("ios", true)
}

expect fun getPlatform(): Platform