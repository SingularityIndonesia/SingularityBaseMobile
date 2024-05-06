package com.singularityindonesia.validation.util

import com.singularityindonesia.validation.ValidationError
import com.singularityindonesia.validation.Validator

fun String.validateWith(validator: Validator): ValidationError? {
    return validator.asIterable()
        .firstOrNull {
            this.matches(it.key)
        }
        ?.value
}