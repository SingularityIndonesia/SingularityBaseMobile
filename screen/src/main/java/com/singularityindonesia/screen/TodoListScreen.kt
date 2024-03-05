package com.singularityindonesia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularityindonesia.screen.TodoListScreenViewModel.Companion.AddFilter
import com.singularityindonesia.screen.TodoListScreenViewModel.Companion.ClearFilter
import com.singularityindonesia.screen.TodoListScreenViewModel.Companion.Search
import com.singularityindonesia.screen.TodoListScreenViewModel.Companion.SelectTodo
import com.singularityindonesia.serialization.PrettyJson
import kotlinx.serialization.encodeToString

data class TodoListScreenPld(
    val unit: Unit = Unit
)

@Composable
fun TodoListScreen(
    pld: TodoListScreenPld = TodoListScreenPld(),
    viewModel: TodoListScreenViewModel = viewModel()
) {
    Column {
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

        SearchComponent(
            clue = searchClue.value,
            onSearch = {
                searchClue.value = it
            }
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

        val list = viewModel.TodoListDisplay.collectAsState(initial = listOf())
        key(list.value) {
            TodoList(
                list = list.value,
                onClick = {
                    viewModel.Post(SelectTodo(it.todo))
                }
            )
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
private fun TodoList(
    list: List<TodoDisplay>,
    onClick: (TodoDisplay) -> Unit
) {
    LazyColumn {
        items(list) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                TodoCard(
                    todo = it,
                    onClick = onClick
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
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
