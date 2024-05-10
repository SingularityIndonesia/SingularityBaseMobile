package com.singularity.validation.companion

import com.singularity.validation.InvalidEmpty
import com.singularity.validation.InvalidFormat
import com.singularity.validation.ValidationError

fun<T> ValidationError.tryFoldEmailValidationError(
    onFormatError: (() -> T)? = null,
    onEmptyError: (()-> T)? = null,
    other: (()->T)? = null
): T {
    return when(this) {
        InvalidFormat -> onFormatError?.invoke() ?: other?.invoke()
        InvalidEmpty -> onEmptyError?.invoke() ?: other?.invoke()
    } ?: run {
        throw IllegalArgumentException("Unhandled ValidationError ${this::class.simpleName}")
    }
}