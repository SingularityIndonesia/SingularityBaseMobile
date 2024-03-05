package com.singularityindonesia.data

import com.singularityindonesia.exception.MException

sealed interface VmState<T>

data class Idle<T>(
    val unit: Unit = Unit
) : VmState<T>

data class Processing<T>(
    val unit: Unit = Unit
) : VmState<T>

data class Success<T>(
    val data: T
) : VmState<T>

data class Failed<T>(
    val e: MException
) : VmState<T>

fun<T,R> VmState<T>.fold(
    onIdle: (()-> R)? = null,
    onProcessing: (()-> R)? = null,
    onSuccess: ((T) -> R)? = null,
    onFailed: ((MException) -> R)? = null,
    onElse: (()-> R)? = null,
): R {
    return when(this) {
        is Idle -> onIdle?.invoke() ?: onElse?.invoke() ?: run { throw NullPointerException("Lambda reducer for idle is null.") }
        is Processing -> onProcessing?.invoke() ?: onElse?.invoke() ?: run { throw NullPointerException("Lambda reducer for onProcessing is null.")}
        is Success -> onSuccess?.invoke(data) ?: onElse?.invoke() ?: run { throw NullPointerException("Lambda reducer for onSuccess is null.") }
        is Failed -> onFailed?.invoke(e) ?: onElse?.invoke() ?: run { throw NullPointerException("Lambda reducer for onFailed is null.") }
    }
}