package com.singularityindonesia.validation

import com.singularityindonesia.regex.EmailRegex
import com.singularityindonesia.regex.NotEmptyRegex

typealias Validator = Map<Regex, ValidationError>

val EmailValidator = mapOf(
    EmailRegex to InvalidFormat,
    NotEmptyRegex to InvalidEmpty
)
