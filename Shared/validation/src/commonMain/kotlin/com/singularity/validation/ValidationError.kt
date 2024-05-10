package com.singularity.validation

sealed interface ValidationError

data object InvalidFormat: ValidationError
data object InvalidEmpty: ValidationError
