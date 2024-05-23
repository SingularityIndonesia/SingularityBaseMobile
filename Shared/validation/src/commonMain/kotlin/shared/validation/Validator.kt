/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package shared.validation

import shared.regex.EmailPattern

typealias Pattern = String
typealias Validator = Map<Pattern, ValidationError>

val EmailValidator = mapOf(
    EmailPattern to InvalidFormat,
    EmailPattern to InvalidEmpty
)


