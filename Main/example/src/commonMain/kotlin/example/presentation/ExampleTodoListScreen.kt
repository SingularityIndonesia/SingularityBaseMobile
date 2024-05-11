/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package example.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singularity.common.PrettyJson
import com.singularity.common.getPlatform
import com.singularity.contex.MainContext
import com.singularity.data.VmFailed
import com.singularity.data.VmIdle
import com.singularity.data.VmProcessing
import com.singularity.data.VmState
import com.singularity.data.VmSuccess
import com.singularity.data.fold
import example.data.GetTodos
import example.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    val todoListDataState: VmState<List<Todo>> = VmIdle(),
) {
    // Reducer
    val statusDisplay: String
        get() =
            todoListDataState
                .fold(
                    ifIdle = { "Idle" },
                    ifProcessing = { "Processing" },
                    ifSuccess = { "Success" },
                    ifFailed = { "Failed" }
                )
                .let {
                    "Status = $it"
                }

    val errorDisplay: String
        get() =
            if (todoListDataState is VmFailed)
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

    val platformName = remember {
        getPlatform().name
    }

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
                    todoListDataState = VmProcessing()
                )
                with(webRepositoryContext) {
                    // BUG: context receiver implementation is still not working.
                    // You need to use basic composition pattern.
                    GetTodos(this.httpClient)
                }
                    .onSuccess {
                        state = state.copy(
                            todoList = it,
                            todoListDataState = VmSuccess(
                                data = it
                            )
                        )
                    }
                    .onFailure {
                        it.also { e ->
                                state = state.copy(
                                    todoListDataState = VmFailed(
                                        e = Exception(e)
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
        state.todoListDataState is VmIdle
    ) {
        onReload.invoke()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Running on $platformName"
        )

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
            onFilter = { filter: TodoFilter ->
                state = if (state.todoFilters.contains(filter))
                    state.copy(
                        todoFilters = state.todoFilters - filter
                    )
                else
                    state.copy(
                        todoFilters = state.todoFilters + filter
                    )
            },
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
