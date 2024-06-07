/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn


// prefer dispatcher default see: https://github.com/Kotlin/kotlinx.coroutines/issues/3205#issuecomment-1300120523
//fun <T> Flow<T>.moveToIO(): Flow<T> =
//    this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.moveToMain(): Flow<T> =
    this.flowOn(Dispatchers.Main)

fun <T> Flow<T>.moveToDefault(): Flow<T> =
    this.flowOn(Dispatchers.Default)