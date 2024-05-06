package com.singularityindonesia.validation.util

import com.singularityindonesia.exception.MUnHandledException
import com.singularityindonesia.validation.InvalidEmpty
import com.singularityindonesia.validation.InvalidFormat
import com.singularityindonesia.validation.ValidationError

fun<T> ValidationError.tryFoldEmailValidationError(
    onFormatError: (() -> T)? = null,
    onEmptyError: (()-> T)? = null,
    other: (()->T)? = null
): T {
    return when(this) {
        InvalidFormat -> onFormatError?.invoke() ?: other?.invoke()
        InvalidEmpty -> onEmptyError?.invoke() ?: other?.invoke()
    } ?: run {
        throw MUnHandledException("Unhandled ValidationError ${this.javaClass.simpleName}")
    }
}