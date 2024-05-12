package shared.validation.companion

import shared.validation.ValidationError
import shared.validation.Validator

fun String.validateWith(validator: Validator): ValidationError? {
    return validator.asIterable()
        .firstOrNull {
            this.matches(it.key.toRegex())
        }
        ?.value
}