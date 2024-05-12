package shared.validation

import shared.regex.EmailPattern

typealias Pattern = String
typealias Validator = Map<Pattern, ValidationError>

val EmailValidator = mapOf(
    EmailPattern to InvalidFormat,
    EmailPattern to InvalidEmpty
)


