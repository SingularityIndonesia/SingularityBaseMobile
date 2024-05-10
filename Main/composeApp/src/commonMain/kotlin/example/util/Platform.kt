package example.util

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform