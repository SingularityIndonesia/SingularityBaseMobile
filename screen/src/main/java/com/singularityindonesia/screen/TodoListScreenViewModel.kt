package com.singularityindonesia.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularityindonesia.analytics.report
import com.singularityindonesia.data.*
import com.singularityindonesia.exception.utils.toException
import com.singularityindonesia.main_context.MainContext
import com.singularityindonesia.model.Todo
import com.singularityindonesia.webrepository.GetTodos
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class TodoListScreenViewModel : ViewModel() {

    private val webRepositoryContext = MainContext.get()

    private val _selectedTodo = MutableStateFlow<Todo?>(null)
    fun setSelectedTodo(todo: Todo) {
        viewModelScope.launch {
            _selectedTodo.emit(todo)
        }
    }

    fun clearSelectedTodo() {
        viewModelScope.launch {
            _selectedTodo.emit(null)
        }
    }

    private val _searchClue = MutableStateFlow("")
    val searchClue = _searchClue.asStateFlow()
    fun search(clue: String) {
        viewModelScope.launch {
            _searchClue.emit(clue)
        }
    }

    private val _todoFilters = MutableStateFlow<List<TodoFilter>>(listOf())
    fun addFilter(filter: TodoFilter) {
        viewModelScope.launch {
            _todoFilters.first()
                .plus(filter)
                .also {
                    _todoFilters.emit(it)
                }
        }
    }

    fun clearFilters() {
        viewModelScope.launch {
            _todoFilters.emit(listOf())
        }
    }

    private val _todoListState = MutableStateFlow<VmState<List<Todo>>>(Idle())
    val todoListDisplay = combine(
        _selectedTodo,
        _todoListState,
        _todoFilters,
        _searchClue,
    ) { selected, todoListState, filters, searchClue ->
        todoListState
            .fold(
                onSuccess = { todoList -> todoList },
                onElse = { listOf() }
            )
            .filter {
                if (searchClue.isBlank())
                    true
                else
                    it.toString().contains(searchClue)
            }
            .filter { todo ->
                filters.fold(true) { acc, l ->
                    when (l) {
                        is ShowCompleteOnly -> acc && todo.completed
                    }
                }
            }
            .map { todo ->
                TodoDisplay(
                    todo = todo,
                    selectable = true,
                    selected = todo.id == selected?.id
                )
            }
    }

    val status = _todoListState.map {
        it::class.simpleName
    }

    val error = _todoListState.map {
        it.fold(
            onFailed = { e -> e.message },
            onElse = { "" }
        )
    }

    val appliedFilters = combine(
        _searchClue,
        _todoFilters
    ) { searchClue, todoFilters ->

        val filters =
            (if (searchClue.isNotBlank())
                listOf("Search: $searchClue")
            else
                listOf())
                .plus(
                    todoFilters.map {
                        it.toString()
                    }
                )
                .fold("") { acc, r ->
                    "$acc$r,"
                }

        "Applied Filters: $filters"
    }

    fun getTodos() {
        viewModelScope.launch {
            _todoListState.emit(Processing())

            with(webRepositoryContext) {
                GetTodos()
            }
                .onSuccess {
                    _todoListState.emit(
                        Success(data = it)
                    )
                }
                .onFailure {
                    it.toException()
                        .also { e ->
                            _todoListState.emit(
                                Failed(e)
                            )
                        }
                        .also { e ->
                            e.report()
                        }
                }
        }
    }

    init {
        getTodos()
    }
}

@Serializable
data class TodoDisplay(
    val todo: Todo,
    val selectable: Boolean,
    val selected: Boolean
)

sealed interface TodoFilter
data object ShowCompleteOnly : TodoFilter