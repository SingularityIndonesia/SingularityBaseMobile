package shared.validation.companion

import shared.ValidationError
import shared.Validator

fun String.validateWith(validator: Validator): ValidationError? {
    return validator.asIterable()
        .firstOrNull {
            this.matches(it.key)
        }
        ?.value
}