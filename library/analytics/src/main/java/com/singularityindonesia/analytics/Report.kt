package com.singularityindonesia.analytics

import android.util.Log
import com.singularityindonesia.exception.MException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun MException.report() {
    GlobalScope.launch {
        withContext(Dispatchers.IO) {
            // fixme: report the exception here
            Log.d("MException", "report: $this")
        }
    }
}