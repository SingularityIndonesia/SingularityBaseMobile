package example.presentation.entity

sealed class TodoFilter {
    data object ShowCompleteOnly: TodoFilter()
}