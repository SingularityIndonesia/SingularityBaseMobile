package example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import common.getPlatform
import common.ifNotBlank
import common.messageOrUnknownError
import common.moveToDefault
import core.data.VmFailed
import core.data.VmIdle
import core.data.VmProcessing
import core.data.VmState
import core.data.VmSuccess
import core.data.fold
import core.lifecycle.StateSaver
import example.data.GetTodos
import example.model.Context
import example.model.Todo
import example.model.TodoID
import example.presentation.entity.TodoDisplay
import example.presentation.entity.TodoFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed class Intent {
    data class Search(val clue: String) : Intent()
    data class SelectTodo(val todoID: TodoID) : Intent()
    data class AddFilter(val filter: TodoFilter) : Intent()
    data class RemoveFilter(val filter: TodoFilter) : Intent()
    data object ClearFilter : Intent()
    data object Reload : Intent()
}

class TodoListPaneState(
    saveAble: SaveAble
) {
    internal val todoListDataState = MutableStateFlow(saveAble.todoListDataState)
    internal val searchClue = MutableStateFlow(saveAble.searchClue)
    internal val todoFilters = MutableStateFlow(saveAble.todoFilters)
    internal val selectedTodoIDs = MutableStateFlow(saveAble.selectedTodoIDs)

    data class SaveAble(
        val todoListDataState: VmState<List<Todo>> = VmIdle(),
        val searchClue: String = "",
        val todoFilters: List<TodoFilter> = listOf(),
        val selectedTodoIDs: List<TodoID> = listOf()
    )

    fun saveAble() = SaveAble(
        todoListDataState = todoListDataState.value,
        searchClue = searchClue.value,
        todoFilters = todoFilters.value,
        selectedTodoIDs = selectedTodoIDs.value
    )
}

class TodoListPaneStateReducer(
    state: TodoListPaneState
) {
    val platformName = flowOf(getPlatform().name)

    val dataIsNotLoaded = state.todoListDataState.map {
        it is VmIdle
    }

    val statusDisplay =
        state.todoListDataState
            .map {
                it.fold(
                    ifIdle = { "Status = Idle" },
                    ifProcessing = { "Status = Processing" },
                    ifSuccess = { "Status = Success" },
                    ifFailed = { "Status = Failed" }
                )
            }
            .moveToDefault()

    val errorDisplay =
        state.todoListDataState
            .map {
                it.fold(ifFailed = { "Error = ${it.message}" }) { "Error = " }
            }
            .moveToDefault()

    val appliedFilters =
        combine(
            state.searchClue,
            state.todoFilters,
        ) { q, filters ->
            val searchFilter = q.ifNotBlank({ listOf("Search: $it") }) { listOf() }
            val filtersString = filters.plus(searchFilter).joinToString { it.toString() }
            "Applied Filters: $filtersString"
        }.moveToDefault()

    val selectedTodoIDs = state.selectedTodoIDs.asStateFlow()
    val searchClue = state.searchClue.asStateFlow()
    val todoFilters = state.todoFilters.asStateFlow()
    val todoListDisplay = combine(
        state.todoListDataState,
        state.searchClue,
        state.todoFilters,
        state.selectedTodoIDs
    ) { todoDataState, q, filters, selectedIDs ->
        val todos = todoDataState.fold(ifSuccess = { it }) { listOf() }
        todos.filter { matchesSearch(it, q) && matchesFilters(it, filters) }
            .map { todo ->
                TodoDisplay(
                    todo = todo,
                    selectable = true,
                    selected = selectedIDs.map { todoID -> todoID.value }
                        .contains(todo.id.toString())
                )
            }
    }.moveToDefault()

    private fun matchesSearch(todo: Todo, q: String): Boolean {
        return q.isBlank() || todo.toString().contains(q)
    }

    private fun matchesFilters(todo: Todo, filters: List<TodoFilter>): Boolean {
        return if (filters.isEmpty()) {
            true
        } else {
            filters.any { filter ->
                when (filter) {
                    is TodoFilter.ShowCompleteOnly -> todo.completed
                    // Add checks for other filter types here
                }
            }
        }
    }

    val listErrorDisplay = state.todoListDataState
        .map {
            it.fold(ifFailed = { it.messageOrUnknownError() }) { "" }
        }
        .moveToDefault()
}

class TodoListPaneViewModel(
    private val context: Context,
    private val defaultState: TodoListPaneState.SaveAble
) : ViewModel() {

    val state = TodoListPaneState(defaultState)
    val reducer = TodoListPaneStateReducer(state)

    fun Post(
        intent: Intent
    ) = viewModelScope.launch {
        when (intent) {
            is Intent.Search -> {
                state.searchClue.emit(intent.clue)
            }

            is Intent.SelectTodo -> {
                state.selectedTodoIDs.emit(
                    // you can select multiple but for now we will demonstrate one
                    //  selectedTodoIDs.first()
                    //      .plus(intent.todoID)
                    //      .distinct()
                    listOf(intent.todoID)
                )
            }

            is Intent.AddFilter -> {
                state.todoFilters.emit(
                    state.todoFilters.first().plus(intent.filter)
                )
            }

            is Intent.RemoveFilter -> {
                state.todoFilters.emit(
                    state.todoFilters.first().minus(intent.filter)
                )
            }

            is Intent.ClearFilter -> {
                state.todoFilters.emit(
                    listOf()
                )
            }

            is Intent.Reload -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            state.todoListDataState.emit(VmProcessing())
            with(context) {
                GetTodos()
                    .onSuccess {
                        state.todoListDataState.emit(
                            VmSuccess(
                                data = it
                            )
                        )
                    }
                    .onFailure {
                        it.also { e ->
                            state.todoListDataState.emit(
                                VmFailed(Exception(e))
                            )
                        }
                    }
            }
        }
    }
}
