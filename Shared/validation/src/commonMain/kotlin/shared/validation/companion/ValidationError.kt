/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package shared.validation.companion

import shared.validation.InvalidEmpty
import shared.validation.InvalidFormat
import shared.validation.ValidationError

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