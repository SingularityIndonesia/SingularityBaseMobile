package shared

import com.singularity.regex.EmailRegex
import com.singularity.regex.NotEmptyRegex

typealias Validator = Map<Regex, ValidationError>

val EmailValidator = mapOf(
    EmailRegex to InvalidFormat,
    NotEmptyRegex to InvalidEmpty
)


