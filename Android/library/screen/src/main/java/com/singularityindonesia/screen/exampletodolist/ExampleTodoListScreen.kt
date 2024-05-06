/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.screen.exampletodolist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singularityindonesia.analytic.report
import com.singularityindonesia.data.*
import com.singularityindonesia.exception.utils.toException
import com.singularityindonesia.main_context.MainContext
import com.singularityindonesia.model.Todo
import com.singularityindonesia.serialization.PrettyJson
import com.singularityindonesia.webrepository.GetTodos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Immutable
@Serializable
data class TodoDisplay(
    val todo: Todo,
    val selectable: Boolean,
    val selected: Boolean
)

sealed interface TodoFilter
data object ShowCompleteOnly : TodoFilter

@Immutable
data class ExampleTodoListScreenPld(
    val unit: Unit = Unit
)

@Immutable
data class ExampleTodoListScreenState(
    val searchClue: String = "",
    val searchError: String = "",
    val todoFilters: List<TodoFilter> = listOf(),
    val selectedTodo: Todo? = null,
    val todoList: List<Todo> = listOf(),
    val todoListDataState: VmState<List<Todo>> = Idle(),
) {
    // Reducer
    val statusDisplay: String
        get() =
            when (todoListDataState) {
                is Idle -> "Idle"
                is Processing -> "Processing"
                is Success -> "Success"
                is Failed -> "Failed"
            }.let {
                "Status = $it"
            }

    val errorDisplay: String
        get() =
            if (todoListDataState is Failed)
                "Error = ${todoListDataState.e.message}"
            else
                "Error = "

    val appliedFilters: String
        get() =
            todoFilters
                .plus(
                    if (searchClue.isNotBlank()) {
                        listOf("Search: $searchClue")
                    } else {
                        listOf()
                    }
                )
                .joinToString {
                    it.toString()
                }
                .let {
                    "Applied Filters = $it"
                }

    val todoListDisplay: List<TodoDisplay>
        get() =
            todoList
                .filter {
                    if (searchClue.isBlank())
                        true
                    else
                        it.toString().contains(searchClue)
                }
                .filter {
                    if (todoFilters.isEmpty())
                        true
                    else
                        todoFilters.any { filter ->
                            when (filter) {
                                is ShowCompleteOnly -> it.completed
                            }
                        }
                }
                .map {
                    TodoDisplay(
                        todo = it,
                        selectable = true,
                        selected = selectedTodo?.id == it.id
                    )
                }

    val listErrorDisplay: String
        get() =
            todoListDataState
                .fold(
                    ifFailed = {
                        it.message
                    }
                ) {
                    null
                }
                ?: searchError
}

@Composable
fun ExampleTodoListScreen(
    pld: ExampleTodoListScreenPld = ExampleTodoListScreenPld(),
) {
    val webRepositoryContext =
        remember {
            MainContext.mainContext.webRepositoryContext
        }

    val ioScope = rememberCoroutineScope()

    var state: ExampleTodoListScreenState
            by remember {
                mutableStateOf(ExampleTodoListScreenState())
            }

    val onSearch =
        remember {
            { clue: String ->
                state = state.copy(
                    searchClue = clue
                )
            }
        }

    val onFilter =
        remember {
            { filter: TodoFilter ->
                state = if (state.todoFilters.contains(filter))
                    state.copy(
                        todoFilters = state.todoFilters - filter
                    )
                else
                    state.copy(
                        todoFilters = state.todoFilters + filter
                    )
            }
        }

    val onClearFilter =
        remember {
            {
                state = state.copy(
                    todoFilters = listOf()
                )
            }
        }

    val onItemClicked =
        remember {
            { todoDisplay: TodoDisplay ->
                state = state.copy(
                    selectedTodo = todoDisplay.todo
                )
            }
        }

    // fixme: cancel operation on resumed
    val onReload = remember {
        {
            ioScope.launch(Dispatchers.IO) {
                state = state.copy(
                    todoListDataState = Processing()
                )
                with(webRepositoryContext) {
                    GetTodos()
                }
                    .onSuccess {
                        state = state.copy(
                            todoList = it,
                            todoListDataState = Success(
                                data = it
                            )
                        )
                    }
                    .onFailure {
                        it.toException()
                            .also { e ->
                                e.report()
                            }
                            .also { e ->
                                state = state.copy(
                                    todoListDataState = Failed(
                                        e = e
                                    )
                                )
                            }
                    }
            }
            Unit
        }
    }

    // preload
    LaunchedEffect(
        state.todoListDataState is Idle
    ) {
        onReload.invoke()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val statusDisplay by remember {
            derivedStateOf {
                state.statusDisplay
            }
        }
        Status(
            status = statusDisplay
        )

        val errorDisplay by remember {
            derivedStateOf {
                state.errorDisplay
            }
        }
        Error(
            error = errorDisplay
        )

        val appliedFilters by remember() {
            derivedStateOf {
                state.appliedFilters
            }
        }
        AppliedFilters(
            appliedFilters = appliedFilters
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        SearchComponent(
            clue = state.searchClue,
            onSearch = onSearch
        )

        Spacer(
            modifier = Modifier.size(8.dp)
        )

        ButtonFilters(
            onFilter = onFilter,
            onClearFilter = onClearFilter
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        val todoListDisplay by remember {
            derivedStateOf {
                state.todoListDisplay
            }
        }
        val errorDisplay2 by remember {
            derivedStateOf {
                state.listErrorDisplay
            }
        }
        val scrollState = rememberLazyListState()
        TodoList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            todoListDisplay = todoListDisplay,
            scrollState = scrollState,
            error = errorDisplay2,
            onReload = onReload,
            onItemClicked = onItemClicked,
        )
    }
}

@Composable
private fun Status(
    status: String
) {
    Text(
        text = status
    )
}

@Composable
private fun Error(
    error: String
) {
    Text(
        text = error
    )
}

@Composable
private fun AppliedFilters(
    appliedFilters: String
) {
    Text(
        text = appliedFilters
    )
}

@Composable
private fun TodoList(
    modifier: Modifier,
    scrollState: LazyListState,
    todoListDisplay: List<TodoDisplay>,
    error: String,
    onReload: () -> Unit,
    onItemClicked: (TodoDisplay) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
    ) {
        items(todoListDisplay) {
            TodoItem(
                item = it,
                onClick = onItemClicked
            )
        }

        item(error) {
            if (error.isNotBlank())
                Reload(
                    error = error,
                    onReload = onReload
                )
        }
    }
}

@Composable
fun Reload(
    error: String,
    onReload: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        16.dp
                    )
            ) {
                Text(
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        ),
                    text = error
                )
                Button(
                    modifier = Modifier
                        .padding(
                            top = 16.dp
                        )
                        .align(
                            Alignment.End
                        ),
                    onClick = onReload
                ) {
                    Text(
                        text = "Reload"
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonFilters(
    onFilter: (TodoFilter) -> Unit,
    onClearFilter: () -> Unit
) {
    Row {

        Spacer(
            modifier = Modifier
                .size(
                    16.dp
                )
        )
        Button(
            onClick = {
                onFilter.invoke(
                    ShowCompleteOnly
                )
            }
        ) {
            Text(
                text = "Completed"
            )
        }

        Spacer(
            modifier = Modifier
                .size(
                    8.dp
                )
        )
        Button(
            onClick = {
                onClearFilter.invoke()
            }
        ) {
            Text(
                text = "Show All"
            )
        }

    }
}

@Composable
private fun SearchComponent(
    clue: String,
    onSearch: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .fillMaxWidth(),
        value = clue,
        onValueChange = onSearch
    )
}

@Composable
fun TodoItem(
    item: TodoDisplay,
    onClick: (TodoDisplay) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
    ) {
        TodoCard(
            todo = item,
            onClick = onClick
        )
        Spacer(
            modifier = Modifier
                .size(
                    8.dp
                )
        )
    }
}

@Composable
fun TodoCard(
    todo: TodoDisplay,
    onClick: (TodoDisplay) -> Unit
) {
    Card(
        onClick = {
            onClick.invoke(
                todo
            )
        },
        colors = if (todo.selected)
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        else
            CardDefaults.cardColors(),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    16.dp
                )
        ) {
            Text(
                text = PrettyJson
                    .encodeToString(todo)
            )
        }
    }
}
