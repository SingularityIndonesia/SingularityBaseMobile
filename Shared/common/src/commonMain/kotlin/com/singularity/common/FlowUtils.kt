package com.singularity.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn


fun <T> Flow<T>.moveToIO(): Flow<T> =
    this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.moveToMain(): Flow<T> =
    this.flowOn(Dispatchers.Main)

fun <T> Flow<T>.moveToDefault(): Flow<T> =
    this.flowOn(Dispatchers.Default)