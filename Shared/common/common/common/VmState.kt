/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


sealed interface VmState<T>

@OptIn(ExperimentalContracts::class)
fun<T> VmState<T>.isFailed(): Boolean {
    contract {
        returns(true) implies (this@isFailed is VmFailed)
    }
    return this is VmFailed
}

@OptIn(ExperimentalContracts::class)
fun<T> VmState<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is VmSuccess)
        }
    return this is VmSuccess
}

@OptIn(ExperimentalContracts::class)
fun<T> VmState<T>.isProcessing(): Boolean {
    contract {
        returns(true) implies (this@isProcessing is VmProcessing)
    }
    return this is VmProcessing
}

@OptIn(ExperimentalContracts::class)
fun<T> VmState<T>.isIdle(): Boolean {
    contract {
        returns(true) implies (this@isIdle is VmIdle)
    }
    return this is VmIdle
}

data class VmIdle<T>(
    val unit: Unit = Unit
) : VmState<T>

data class VmProcessing<T>(
    val unit: Unit = Unit
) : VmState<T>

data class VmSuccess<T>(
    val data: T
) : VmState<T>

data class VmFailed<T>(
    val e: Exception
) : VmState<T>

/**
 * @param ifIdle if null, fallback will be invoked
 * @param ifProcessing if null, fallback will be invoked
 * @param ifSuccess if null, fallback will be invoked
 * @param ifFailed if null, fallback will be invoked
 * @param fallback if any required parameter is null, fallback will be invoked
 */
inline fun <reified T, R> VmState<T>.fold(
    noinline ifIdle: (() -> R)? = null,
    noinline ifProcessing: (() -> R)? = null,
    noinline ifSuccess: ((T) -> R)? = null,
    noinline ifFailed: ((Exception) -> R)? = null,
    noinline fallback: (() -> R)? = null
): R {
    // argument contract
    require(fallback != null || ifIdle != null) {
        "When ifElse is unset, ifIdle must be set and vice versa."
    }
    require(fallback != null || ifProcessing != null) {
        "When ifElse is unset, ifProcessing must be set and vice versa."
    }
    require(fallback != null || ifSuccess != null) {
        "When ifElse is unset, ifSuccess must be set and vice versa."
    }
    require(fallback != null || ifFailed != null) {
        "When ifElse is unset, ifFailed must be set and vice versa."
    }

    return when (this) {
        is VmIdle ->
            if (ifIdle != null)
                ifIdle.invoke()
            else
                fallback!!.invoke()

        is VmProcessing ->
            if (ifProcessing != null)
                ifProcessing.invoke()
            else
                fallback!!.invoke()

        is VmSuccess ->
            if (ifSuccess != null)
                ifSuccess.invoke(data)
            else
                fallback!!.invoke()

        is VmFailed ->
            if (ifFailed != null)
                ifFailed.invoke(e)
            else
                fallback!!.invoke()
    }
}

inline fun <reified T, R> Flow<VmState<T>>.foldEach(
    noinline onIdle: (() -> R)? = null,
    noinline onProcessing: (() -> R)? = null,
    noinline onSuccess: ((T) -> R)? = null,
    noinline onFailed: ((Exception) -> R)? = null,
    noinline default: (() -> R)? = null,
): Flow<R> =
    map {
        it.fold(
            ifIdle = onIdle,
            ifProcessing = onProcessing,
            ifSuccess = onSuccess,
            ifFailed = onFailed,
            fallback = default
        )
    }