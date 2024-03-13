/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.analytic

import android.util.Log
import com.singularityindonesia.exception.MException
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
fun MException.report() {
    GlobalScope.launch {
        withContext(Dispatchers.IO) {
            // fixme: report the exception here
            Log.d("MException", "report: $this")
        }
    }
}