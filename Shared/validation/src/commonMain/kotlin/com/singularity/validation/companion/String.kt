package com.singularity.validation.companion

import com.singularity.validation.ValidationError
import com.singularity.validation.Validator

fun String.validateWith(validator: Validator): ValidationError? {
    return validator.asIterable()
        .firstOrNull {
            this.matches(it.key)
        }
        ?.value
}