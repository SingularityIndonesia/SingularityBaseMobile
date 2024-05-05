package com.singularityindonesia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

context(ViewModel)
fun <T> Flow<T>.shareWhileSubscribed(
    default: T,
    stopTimeoutMillis: Long = 0,
    replayExpirationMillis: Long = Long.MAX_VALUE,
): Flow<T> =
    this.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(
            stopTimeoutMillis,
            replayExpirationMillis
        ),
        default
    )