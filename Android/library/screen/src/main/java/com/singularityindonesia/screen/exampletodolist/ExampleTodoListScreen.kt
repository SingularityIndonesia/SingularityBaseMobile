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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularityindonesia.screen.exampletodolist.TodoListScreenViewModel.Companion.AddFilter
import com.singularityindonesia.screen.exampletodolist.TodoListScreenViewModel.Companion.ClearFilter
import com.singularityindonesia.screen.exampletodolist.TodoListScreenViewModel.Companion.Reload
import com.singularityindonesia.screen.exampletodolist.TodoListScreenViewModel.Companion.Search
import com.singularityindonesia.screen.exampletodolist.TodoListScreenViewModel.Companion.SelectTodo
import com.singularityindonesia.serialization.PrettyJson
import kotlinx.serialization.encodeToString

@Immutable
data class TodoListScreenPld(
    val unit: Unit = Unit
)

@Composable
fun ExampleTodoListScreen(
    pld: TodoListScreenPld = TodoListScreenPld(),
    viewModel: TodoListScreenViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val status by viewModel.Status.collectAsState(initial = "Idle")
        Text(text = "Status = $status")

        val error by viewModel.Error.collectAsState(initial = "No Error")
        Text(text = "Error = ${error.ifBlank { "No Error" }}")

        val filters by viewModel.AppliedFilters.collectAsState(initial = "")
        Text(text = filters)

        Spacer(modifier = Modifier.size(16.dp))

        val searchClue = remember {
            mutableStateOf("")
        }

        LaunchedEffect(
            key1 = searchClue.value,
            block = {
                viewModel.Post(Search(searchClue.value))
            }
        )

        val onSearch = remember {
            { clue: String ->
                searchClue.value = clue
            }
        }

        SearchComponent(
            clue = searchClue.value,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.size(8.dp))

        val onFilter = remember {
            { filter: TodoFilter ->
                viewModel.Post(AddFilter(filter))
            }
        }
        val onClearFilter = remember {
            {
                viewModel.Post(ClearFilter)
            }
        }
        ButtonFilters(
            onFilter = onFilter,
            onClearFilter = onClearFilter
        )

        Spacer(modifier = Modifier.size(16.dp))

        val list by viewModel.TodoListDisplay.collectAsState(initial = listOf())
        val scrollState = rememberLazyListState()
        val onItemClicked = remember {
            { todoDisplay: TodoDisplay ->
                viewModel.Post(SelectTodo(todoDisplay.todo))
            }
        }
        val searchError by viewModel.SearchError.collectAsState(initial = null)
        LazyColumn(
            state = scrollState,
        ) {
            items(list) {
                TodoItem(
                    item = it,
                    onClick = onItemClicked
                )
            }
            if (error.isNotBlank())
                item(error) {
                    Button(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = {
                            viewModel.Post(Reload())
                        }
                    ) {
                        Text(text = "Reload")
                    }
                }

            if (searchError != null)
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
                                modifier = Modifier.padding(16.dp)
                                    .align(Alignment.CenterHorizontally),
                                text = searchError!!
                            )
                        }
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
