package main.example.presentation.entity

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import main.example.model.Todo

@Immutable
@Serializable
data class TodoDisplay(
    val todo: Todo,
    val selectable: Boolean,
    val selected: Boolean
)