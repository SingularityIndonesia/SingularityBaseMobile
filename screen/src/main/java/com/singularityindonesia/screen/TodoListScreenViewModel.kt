package com.singularityindonesia.screen

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularityindonesia.analytics.report
import com.singularityindonesia.data.*
import com.singularityindonesia.exception.UnHandledException
import com.singularityindonesia.exception.utils.toException
import com.singularityindonesia.main_context.MainContext
import com.singularityindonesia.model.Todo
import com.singularityindonesia.webrepository.GetTodos
import com.singularityindonesia.webrepository.WebRepositoryContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class TodoListScreenViewModel(
    private val webRepositoryContext: WebRepositoryContext = MainContext.get()
) : ViewModel() {

    companion object {
        sealed interface ViewModelIntent
        data class SelectTodo(val todo: Todo) : ViewModelIntent
        data object ClearSelectedTodo : ViewModelIntent
        data class Search(val clue: String) : ViewModelIntent
        data class AddFilter(val filter: TodoFilter) : ViewModelIntent
        data object ClearFilter : ViewModelIntent
        data class Reload(
            val signature: String = System.currentTimeMillis().toString()
        ) : ViewModelIntent
    }

    private val intent = MutableSharedFlow<ViewModelIntent>(100)
    fun Post(intent: ViewModelIntent) {
        viewModelScope.launch {
            this@TodoListScreenViewModel.intent.emit(intent)
        }
    }

    private val selectedTodo: Flow<Todo?> =
        intent
            .filter { it is SelectTodo || it is ClearSelectedTodo }
            .flowOn(Dispatchers.IO)
            .map { intent ->
                when (intent) {
                    is ClearSelectedTodo -> null
                    is SelectTodo -> intent.todo
                    else -> throw UnHandledException(intent.toString())
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val searchClue =
        intent
            .filter { it is Search }
            .flowOn(Dispatchers.IO)
            .map { (it as Search).clue }
            .stateIn(viewModelScope, SharingStarted.Lazily, "")

    private val todoFilters =
        intent
            .filter {
                it is AddFilter || it is ClearFilter
            }
            .flowOn(Dispatchers.IO)
            .scan(listOf<TodoFilter>()) { acc, next ->
                when (next) {
                    is ClearFilter -> listOf()
                    is AddFilter -> {
                        if (acc.contains(next.filter))
                            acc
                        else
                            acc.plus(next.filter)
                    }

                    else -> throw UnHandledException(next.toString())
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())


    @OptIn(ExperimentalCoroutinesApi::class)
    private val todoListState =
        intent
            .filterIsInstance<Reload>()
            .map { it.signature }
            .distinctUntilChanged()
            .flatMapLatest {
                flow<VmState<List<Todo>>> {
                    emit(Processing())
                    with(webRepositoryContext) {
                        GetTodos()
                    }
                        .onSuccess {
                            emit(
                                Success(data = it)
                            )
                        }
                        .onFailure {
                            it.toException()
                                .also { e ->
                                    emit(
                                        Failed(e)
                                    )
                                }
                                .also { e ->
                                    e.report()
                                }
                        }
                }
            }
            .cancellable()
            .stateIn(viewModelScope, SharingStarted.Eagerly, Idle())

    /** ## Reduced State **/
    val TodoListDisplay = combine(
        selectedTodo,
        todoListState,
        todoFilters,
        searchClue,
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
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    val Status = todoListState.map {
        it::class.simpleName
    }

    val Error = todoListState.map {
        it.fold(
            onFailed = { e -> e.message },
            onElse = { "" }
        )
    }

    val AppliedFilters = combine(
        searchClue,
        todoFilters
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

        "Applied Filters = $filters"
    }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val SearchClue =
        searchClue
            .stateIn(viewModelScope, SharingStarted.Lazily, "")

    init {
        Post(Reload())
    }
}

@Immutable
@Serializable
data class TodoDisplay(
    val todo: Todo,
    val selectable: Boolean,
    val selected: Boolean
)

sealed interface TodoFilter
data object ShowCompleteOnly : TodoFilter