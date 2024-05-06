/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.data

import com.singularityindonesia.exception.MException
import com.singularityindonesia.exception.MNullPointerException

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
    ofIdle: (()-> R)? = null,
    ifProcessing: (()-> R)? = null,
    ifSuccess: ((T) -> R)? = null,
    ifFailed: ((MException) -> R)? = null,
    ifElse: (()-> R)? = null,
): R {
    return when(this) {
        is Idle -> ofIdle?.invoke() ?: ifElse?.invoke() ?: throw MNullPointerException("Lambda reducer for idle is null.")
        is Processing -> ifProcessing?.invoke() ?: ifElse?.invoke() ?: throw  MNullPointerException("Lambda reducer for onProcessing is null.")
        is Success -> ifSuccess?.invoke(data) ?: ifElse?.invoke() ?: throw  MNullPointerException("Lambda reducer for onSuccess is null.")
        is Failed -> ifFailed?.invoke(e) ?: ifElse?.invoke() ?: throw  MNullPointerException("Lambda reducer for onFailed is null.")
    }
}