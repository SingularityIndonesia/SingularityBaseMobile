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

inline fun <reified T, R> VmState<T>.fold(
    noinline ifIdle: (() -> R)? = null,
    noinline ifProcessing: (() -> R)? = null,
    noinline ifSuccess: ((T) -> R)? = null,
    noinline ifFailed: ((MException) -> R)? = null,
    noinline ifElse: (() -> R)? = null
): R {
    // argument contract
    require(ifElse != null || ifIdle != null) {
        "When ifElse is unset, ifIdle must be set and vice versa."
    }
    require(ifElse != null || ifProcessing != null) {
        "When ifElse is unset, ifProcessing must be set and vice versa."
    }
    require(ifElse != null || ifSuccess != null) {
        "When ifElse is unset, ifSuccess must be set and vice versa."
    }
    require(ifElse != null || ifFailed != null) {
        "When ifElse is unset, ifFailed must be set and vice versa."
    }

    return when (this) {
        is Idle ->
            if (ifIdle != null)
                ifIdle.invoke()
            else
                ifElse!!.invoke()

        is Processing ->
            if (ifProcessing != null)
                ifProcessing.invoke()
            else
                ifElse!!.invoke()

        is Success ->
            if (ifSuccess != null)
                ifSuccess.invoke(data)
            else
                ifElse!!.invoke()

        is Failed ->
            if (ifFailed != null)
                ifFailed.invoke(e)
            else
                ifElse!!.invoke()
    }
}