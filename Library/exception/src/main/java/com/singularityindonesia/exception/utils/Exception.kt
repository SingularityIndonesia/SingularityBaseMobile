/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.exception.utils

import com.singularityindonesia.exception.MException
import com.singularityindonesia.exception.MWildException

fun Throwable.toException(): MException {
    return if (this is MException)
        this as MException
    else
        MWildException(
            "${this::class.simpleName}: ${this.localizedMessage ?: this.message ?: "Unknown Error"}",
            this
        )
}