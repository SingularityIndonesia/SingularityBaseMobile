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
    if (ifElse == null) {
        // all argument mustn't null
        if (ifIdle == null)
            throw MNullPointerException("Lambda reducer for idle is null.")
        if (ifProcessing == null)
            throw MNullPointerException("Lambda reducer for processing is null.")
        if (ifSuccess == null)
            throw MNullPointerException("Lambda reducer for success is null.")
        if (ifFailed == null)
            throw MNullPointerException("Lambda reducer for failed is null.")
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