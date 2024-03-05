package com.singularityindonesia.exception.utils

import com.singularityindonesia.exception.MException
import com.singularityindonesia.exception.WildException

fun Throwable.toException(): MException {
    return if (this is MException)
        this as MException
    else
        WildException(
            "${this::class.simpleName}: ${this.localizedMessage ?: this.message ?: "Unknown Error"}",
            this
        )
}