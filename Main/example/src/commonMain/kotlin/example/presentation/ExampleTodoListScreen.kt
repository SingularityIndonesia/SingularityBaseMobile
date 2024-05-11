/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package example.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.singularity.common.PrettyJson
import com.singularity.common.getPlatform
import com.singularity.contex.MainContext
import com.singularity.contex.WebRepositoryContext
import com.singularity.data.VmFailed
import com.singularity.data.VmIdle
import com.singularity.data.VmProcessing
import com.singularity.data.VmState
import com.singularity.data.VmSuccess
import com.singularity.data.fold
import com.singularity.designsystem.LargePadding
import com.singularity.designsystem.MediumPadding
import com.singularity.designsystem.SmallPadding
import com.singularity.designsystem.component.LargeSpacing
import com.singularity.designsystem.component.MediumSpacing
import com.singularity.designsystem.component.TextBody
import com.singularity.designsystem.component.TextLabel
import com.singularity.designsystem.component.TextTitle
import com.singularity.lifecycle.SaveAbleState
import example.data.GetTodos
import example.model.Todo
import example.model.TodoID
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
    val unit: Unit = Unit,
    val goToTodoDetail: (TodoID) -> Unit
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
    pld: ExampleTodoListScreenPld,
    saveAbleState: SaveAbleState
) {
    val webRepositoryContext: WebRepositoryContext = remember {
        MainContext()
    }

    val ioScope = rememberCoroutineScope()

    val platformName = remember {
        getPlatform().name
    }

    var state: ExampleTodoListScreenState
            by remember {
                mutableStateOf(
                    saveAbleState.pop() ?: ExampleTodoListScreenState()
                )
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

    val gotoTodoDetail = remember(pld) {
        { todoID: TodoID ->
            saveAbleState.push(state)
            pld.goToTodoDetail.invoke(todoID)
        }
    }

    val onItemClicked =
        remember {
            { todoDisplay: TodoDisplay ->
                if (state.selectedTodo?.id == todoDisplay.todo.id)
                    gotoTodoDetail.invoke(
                        TodoID(
                            todoDisplay.todo.id.toString()
                        )
                    )
                else
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

        TextTitle(
            text = "Running on $platformName",
            modifier = Modifier
                .padding(
                    horizontal = LargePadding
                )
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

        val appliedFilters by remember {
            derivedStateOf {
                state.appliedFilters
            }
        }
        AppliedFilters(
            appliedFilters = appliedFilters
        )

        LargeSpacing()

        SearchComponent(
            clue = state.searchClue,
            onSearch = onSearch
        )

        MediumSpacing()

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

        LargeSpacing()

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
    TextLabel(
        text = status,
        modifier = Modifier
            .padding(
                horizontal = LargePadding
            )
    )
}

@Composable
private fun Error(
    error: String
) {
    TextLabel(
        text = error,
        modifier = Modifier
            .padding(
                horizontal = LargePadding
            )
    )
}

@Composable
private fun AppliedFilters(
    appliedFilters: String
) {
    TextLabel(
        text = appliedFilters,
        modifier = Modifier
            .padding(
                horizontal = LargePadding
            )
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
                horizontal = LargePadding
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
                        LargePadding
                    )
            ) {
                TextLabel(
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        ),
                    text = error
                )
                Button(
                    modifier = Modifier
                        .padding(
                            top = LargePadding
                        )
                        .align(
                            Alignment.End
                        ),
                    onClick = onReload
                ) {
                    TextLabel(
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

        LargeSpacing()
        Button(
            onClick = {
                onFilter.invoke(
                    ShowCompleteOnly
                )
            }
        ) {
            TextLabel(
                text = "Completed"
            )
        }

        MediumSpacing()
        Button(
            onClick = {
                onClearFilter.invoke()
            }
        ) {
            TextLabel(
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
                horizontal = LargePadding
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
                horizontal = LargePadding
            )
    ) {
        TodoCard(
            todo = item,
            onClick = onClick
        )
        MediumSpacing()
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
                    LargePadding
                )
        ) {
            Column {
                TextBody(
                    text = PrettyJson
                        .encodeToString(todo)
                )

                if (todo.selected)
                    MediumSpacing()

                AnimatedVisibility(todo.selected) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextLabel(
                            text = "Click again to open detail",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    horizontal = LargePadding,
                                    vertical = MediumPadding
                                )
                        )
                    }
                }
            }
        }
    }
}
