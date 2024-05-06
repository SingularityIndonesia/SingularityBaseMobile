package com.singularityindonesia.viewmodel

import com.singularityindonesia.data.VmState
import com.singularityindonesia.data.fold
import com.singularityindonesia.exception.MException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<VmState<T>>.foldEach(
    onIdle: (() -> R)? = null,
    onProcessing: (() -> R)? = null,
    onSuccess: ((T) -> R)? = null,
    onFailed: ((MException) -> R)? = null,
    default: (() -> R)? = null,
): Flow<R> =
    map {
        it.fold(
            onIdle = onIdle,
            onProcessing = onProcessing,
            onSuccess = onSuccess,
            onFailed = onFailed,
            onElse = default
        )
    }