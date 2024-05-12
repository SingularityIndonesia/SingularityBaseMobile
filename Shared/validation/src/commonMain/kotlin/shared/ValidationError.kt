package shared

sealed interface ValidationError

data object InvalidFormat: ValidationError
data object InvalidEmpty: ValidationError
