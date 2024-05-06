package com.singularityindonesia.viewmodel

import com.singularityindonesia.data.VmState
import com.singularityindonesia.data.fold
import com.singularityindonesia.exception.MException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T, R> Flow<VmState<T>>.foldEach(
    noinline onIdle: (() -> R)? = null,
    noinline onProcessing: (() -> R)? = null,
    noinline onSuccess: ((T) -> R)? = null,
    noinline onFailed: ((MException) -> R)? = null,
    noinline default: (() -> R)? = null,
): Flow<R> =
    map {
        it.fold(
            ifIdle = onIdle,
            ifProcessing = onProcessing,
            ifSuccess = onSuccess,
            ifFailed = onFailed,
            ifElse = default
        )
    }