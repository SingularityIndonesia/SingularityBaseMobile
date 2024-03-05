package com.singularityindonesia.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularityindonesia.serialization.PrettyJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class TodoListScreenPld(
    val unit: Unit = Unit
)

@Composable
fun TodoListScreen(
    pld: TodoListScreenPld = TodoListScreenPld(),
    viewModel: TodoListScreenViewModel = viewModel()
) {
    Surface {
        Column {
            val status by viewModel.status.collectAsState(initial = "Idle")
            Text(text = "Status = $status")

            val error by viewModel.error.collectAsState(initial = "No Error")
            Text(text = "Error = ${error.ifBlank { "No Error" }}")

            val filters by viewModel.appliedFilters.collectAsState(initial = "")
            Text(text = filters)

            Spacer(modifier = Modifier.size(16.dp))

            val searchClue by viewModel.searchClue.collectAsState()
            TextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                value = searchClue,
                onValueChange = {
                    viewModel.search(it)
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row {

                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        viewModel.addFilter(ShowCompleteOnly)
                    }
                ) {
                    Text(text = "Completed")
                }

                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = {
                        viewModel.clearFilters()
                    }
                ) {
                    Text(text = "Show All")
                }

            }


            Spacer(modifier = Modifier.size(16.dp))

            val list by viewModel.todoListDisplay.collectAsState(initial = listOf())
            LazyColumn {
                items(list) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TodoCard(todo = it) {
                            viewModel.setSelectedTodo(it.todo)
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(todo: TodoDisplay, onClick: (TodoDisplay) -> Unit) {

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
