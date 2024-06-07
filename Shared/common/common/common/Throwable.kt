package common

fun Throwable.messageOrUnknownError(): String {
    return message ?: "Unknown error"
}