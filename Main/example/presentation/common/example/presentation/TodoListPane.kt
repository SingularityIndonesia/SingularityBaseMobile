/*
 * Copyright (c) 2024 Singularity Indonesia (stefanus.ayudha@gmail.com)
 * You are not allowed to remove the copyright. Unless you have a "free software" licence.
 */
package example.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import common.PrettyJson
import common.StateSaver
import designsystem.LargePadding
import designsystem.MediumPadding
import designsystem.component.LargeSpacing
import designsystem.component.MediumSpacing
import designsystem.component.TextBody
import designsystem.component.TextLabel
import designsystem.component.TextTitle
import example.model.Context
import example.model.TodoID
import example.presentation.entity.TodoDisplay
import example.presentation.entity.TodoFilter
import kotlinx.serialization.encodeToString

@Immutable
data class TodoListPanePld(
    val unit: Unit = Unit,
)

@Composable
fun Context.TodoListPane(
    pld: TodoListPanePld,
    stateSaver: StateSaver,
    goToTodoDetail: (TodoID) -> Unit
) {

    val vm = viewModel<TodoListPaneViewModel>(
        factory = viewModelFactory {
            initializer {
                TodoListPaneViewModel(
                    this@TodoListPane,
                    stateSaver.pop() ?: TodoListPaneState.SaveAble()
                )
            }
        }
    )
    val states = vm.reducer

    // preload
    LaunchedEffect(
        states.dataIsNotLoaded
    ) {
        vm.Post(Intent.Reload)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .imePadding()
    ) {

        val platformName by states.platformName.collectAsState("")
        TextTitle(
            label = "Running on $platformName",
            modifier = Modifier.padding(horizontal = LargePadding)
        )

        val status by states.statusDisplay.collectAsState("")
        Status(status = status)

        val errorDisplay by states.errorDisplay.collectAsState("")
        Error(error = errorDisplay)

        val appliedFilters by states.appliedFilters.collectAsState("")
        AppliedFilters(appliedFilters = appliedFilters)

        LargeSpacing()
        var clue by remember { mutableStateOf(states.searchClue.value) }
        SearchComponent(
            clue,
            onSearch = { q: String ->
                clue = q
                vm.Post(Intent.Search(q))
            }
        )

        MediumSpacing()
        ButtonFilters(
            onFilter = { filter: TodoFilter ->
                val isAlreadySelected = states.todoFilters.value.contains(filter)
                if (isAlreadySelected)
                    vm.Post(Intent.RemoveFilter(filter))
                else
                    vm.Post(Intent.AddFilter(filter))
            },
            onClearFilter = { vm.Post(Intent.ClearFilter) }
        )

        LargeSpacing()

        val todoListDisplay by states.todoListDisplay.collectAsState(listOf())
        val errorDisplay2 by states.listErrorDisplay.collectAsState("")
        val scrollState = rememberLazyListState()

        LaunchedEffect(todoListDisplay) {
            val selectedIndex = todoListDisplay
                .indexOfFirst { it.selected }
                .let { if (it < 0) null else it }

            if (selectedIndex != null)
                scrollState.animateScrollToItem(selectedIndex)
        }
        TodoList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            todoListDisplay = todoListDisplay,
            scrollState = scrollState,
            error = errorDisplay2,
            onReload = { vm.Post(Intent.Reload) },
            onItemClicked = { todoDisplay: TodoDisplay ->
                val isAlreadySelected = states.selectedTodoIDs.value.contains(todoDisplay.todoID)
                if (isAlreadySelected) {
                    // save current state
                    stateSaver.pushOrAmend(vm.state.saveAble())
                    goToTodoDetail.invoke(todoDisplay.todoID)
                } else {
                    vm.Post(Intent.SelectTodo(todoDisplay.todoID))
                }
            },
        )
    }
}

@Composable
private fun Status(
    status: String
) {
    TextLabel(
        label = status,
        modifier = Modifier.padding(horizontal = LargePadding)
    )
}

@Composable
private fun Error(
    error: String
) {
    TextLabel(
        label = error,
        modifier = Modifier.padding(horizontal = LargePadding)
    )
}

@Composable
private fun AppliedFilters(
    appliedFilters: String
) {
    TextLabel(
        label = appliedFilters,
        modifier = Modifier.padding(horizontal = LargePadding)
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
            .padding(horizontal = LargePadding)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(LargePadding)
            ) {
                TextLabel(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    label = error
                )
                Button(
                    modifier = Modifier
                        .padding(top = LargePadding)
                        .align(Alignment.End),
                    onClick = onReload
                ) {
                    TextLabel(
                        label = "Reload"
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
            onClick = { onFilter.invoke(TodoFilter.ShowCompleteOnly) }
        ) {
            TextLabel(
                label = "Completed"
            )
        }

        MediumSpacing()
        Button(
            onClick = { onClearFilter.invoke() }
        ) {
            TextLabel(
                label = "Show All"
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
            .padding(horizontal = LargePadding)
            .fillMaxWidth(),
        value = clue,
        onValueChange = { onSearch.invoke(it) },
    )
}

@Composable
fun TodoItem(
    item: TodoDisplay,
    onClick: (TodoDisplay) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = LargePadding)
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
        onClick = { onClick.invoke(todo) },
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
                .padding(LargePadding)
        ) {
            Column {
                TextBody(
                    label = PrettyJson
                        .encodeToString(todo)
                )

                if (todo.selected)
                    MediumSpacing()

                AnimatedVisibility(todo.selected) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextLabel(
                            label = "Click again to open detail",
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
