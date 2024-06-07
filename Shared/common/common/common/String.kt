package common

fun<T> String.ifNotBlank(bloc: (String) -> T, fallback: () -> T): T {
    return if (this.isNotBlank()) bloc(this) else fallback()
}