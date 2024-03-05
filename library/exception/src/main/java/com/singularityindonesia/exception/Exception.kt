package com.singularityindonesia.exception

sealed class MException(override val message: String) : Exception()
data class WildException(override val message: String, val throwable: Throwable?) : MException(message)
data class NullPointerException(override val message: String) : MException(message)
data class IOException(override val message: String) : MException(message)
data class UnHandledException(override val message: String) : MException(message)
