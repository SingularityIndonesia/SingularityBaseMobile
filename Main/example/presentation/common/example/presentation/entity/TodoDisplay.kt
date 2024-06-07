package example.presentation.entity

import androidx.compose.runtime.Immutable
import example.model.Todo
import example.model.TodoID
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class TodoDisplay(
    val todo: Todo,
    val selectable: Boolean,
    val selected: Boolean
) {
    val todoID get() = TodoID(todo.id.toString())
}