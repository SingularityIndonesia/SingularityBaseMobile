/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.screen.exampletodolist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
data class TodoListScreenPld(
    val unit: Unit = Unit
)

@Immutable
data class ExampleTodoListScreenState(
    val error: String = "No Error",
    val searchClue: String = "",
    val searchError: String = "",
    val todoFilters: List<TodoFilter> = listOf(),
    val selectedTodo: Todo? = null,
    val todoList: List<Todo> = listOf(),
    val todoListDataState: VmState<List<Todo>> = Idle(),
)

@Immutable
@Serializable
data class TodoDisplay(
    val todo: Todo,
    val selectable: Boolean,
    val selected: Boolean
)

sealed interface TodoFilter
data object ShowCompleteOnly : TodoFilter

@Composable
fun ExampleTodoListScreen(
    pld: TodoListScreenPld = TodoListScreenPld(),
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
                state = state.copy(searchClue = clue)
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
                            todoListDataState = Success(data = it)
                        )
                    }
                    .onFailure {
                        it.toException()
                            .also { e ->
                                e.report()
                            }
                            .also { e ->
                                state = state.copy(
                                    todoListDataState = Failed(e)
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

        Status(
            status = state.todoListDataState
        )

        Error(
            error = state.error
        )

        AppliedFilters(
            searchClue = state.searchClue,
            todoFilters = state.todoFilters
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        SearchComponent(
            clue = state.searchClue,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.size(8.dp))

        ButtonFilters(
            onFilter = onFilter,
            onClearFilter = onClearFilter
        )

        Spacer(modifier = Modifier.size(16.dp))

        TodoList(
            todoList = state.todoList,
            selectedTodo = state.selectedTodo,
            searchClue = state.searchClue,
            searchError = state.searchError,
            todoFilters = state.todoFilters,
            onItemClicked = onItemClicked,
            onReload = onReload
        )
    }
}

@Composable
private fun Status(
    status: VmState<List<Todo>>
) {
    val statusDisplay by remember(status) {
        derivedStateOf {
            when (status) {
                is Idle -> "Idle"
                is Processing -> "Processing"
                is Success -> "Success"
                is Failed -> "Failed"
            }
                .let {
                    "Status = $it"
                }

        }
    }
    Text(
        text = statusDisplay
    )
}

@Composable
private fun Error(
    error: String
) {
    val errorDisplay by remember {
        derivedStateOf {
            "Error = ${error.ifBlank { "No Error" }}"
        }
    }

    Text(
        text = errorDisplay
    )
}

@Composable
private fun AppliedFilters(
    searchClue: String,
    todoFilters: List<TodoFilter>,
) {
    val appliedFilters by remember(
        key1 = todoFilters + searchClue
    ) {
        derivedStateOf {
            val search = if (searchClue.isNotBlank()) {
                listOf("Search: ${searchClue}")
            } else {
                listOf()
            }
            (search + todoFilters)
                .joinToString {
                    it.toString()
                }
                .let {
                    "Applied Filters = $it"
                }
        }
    }

    Text(
        text = appliedFilters
    )
}

@Composable
private fun TodoList(
    todoList: List<Todo>,
    selectedTodo: Todo?,
    searchClue: String,
    searchError: String,
    todoFilters: List<TodoFilter>,
    onItemClicked: (TodoDisplay) -> Unit,
    onReload: () -> Unit,
) {

    val todoListDisplay by remember(
        key1 = todoList.toString()
                + selectedTodo.toString()
                + searchClue
                + searchError
                + todoFilters.toString()
    ) {
        derivedStateOf {
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
        }
    }
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
    ) {
        items(todoListDisplay) {
            TodoItem(
                item = it,
                onClick = onItemClicked
            )
        }
        if (searchError.isNotBlank())
            item(searchError) {
                Button(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = onReload
                ) {
                    Text(text = "Reload")
                }
            }

        if (searchError.isNotBlank())
            item(searchError) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            text = searchError
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

        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = {
                onFilter.invoke(ShowCompleteOnly)
            }
        ) {
            Text(text = "Completed")
        }

        Spacer(modifier = Modifier.size(8.dp))
        Button(
            onClick = {
                onClearFilter.invoke()
            }
        ) {
            Text(text = "Show All")
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
            .padding(horizontal = 16.dp)
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
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        TodoCard(
            todo = item,
            onClick = onClick
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    todo: TodoDisplay,
    onClick: (TodoDisplay) -> Unit
) {
    Card(
        onClick = {
            onClick.invoke(todo)
        },
        colors = if (todo.selected)
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        else
            CardDefaults.cardColors(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = PrettyJson.encodeToString(todo))
        }
    }
}
