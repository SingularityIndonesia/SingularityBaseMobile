package shared.validation

sealed interface ValidationError

data object InvalidFormat: ValidationError
data object InvalidEmpty: ValidationError
