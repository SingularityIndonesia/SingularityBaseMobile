/*
 * Copyright (c) 2024 Stefanus Ayudha (stefanus.ayudha@gmail.com)
 * Created on 04/03/2024 12:00
 * You are not allowed to remove the copyright.
 */
package com.singularityindonesia.screen.todolist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularityindonesia.analytic.report
import com.singularityindonesia.data.*
import com.singularityindonesia.exception.MUnHandledException
import com.singularityindonesia.exception.utils.toException
import com.singularityindonesia.flow.moveToIO
import com.singularityindonesia.flow.shareWhileSubscribed
import com.singularityindonesia.main_context.MainContext
import com.singularityindonesia.model.Todo
import com.singularityindonesia.webrepository.GetTodos
import com.singularityindonesia.webrepository.WebRepositoryContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Immutable
class TodoListScreenViewModel(
    private val webRepositoryContext: WebRepositoryContext = MainContext()
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
            .map { intent ->
                when (intent) {
                    is ClearSelectedTodo -> null
                    is SelectTodo -> intent.todo
                    else -> throw MUnHandledException(intent.toString())
                }
            }
            .shareWhileSubscribed(null)

    private val searchClue =
        intent
            .filter { it is Search }
            .map { (it as Search).clue }
            .shareWhileSubscribed("")

    private val todoFilters =
        intent
            .filter {
                it is AddFilter || it is ClearFilter
            }
            .scan(listOf<TodoFilter>()) { acc, next ->
                when (next) {
                    is ClearFilter -> listOf()
                    is AddFilter -> {
                        if (acc.contains(next.filter))
                            acc
                        else
                            acc.plus(next.filter)
                    }

                    else -> throw MUnHandledException(next.toString())
                }
            }
            .shareWhileSubscribed(listOf())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val todoListState =
        intent
            .filterIsInstance<Reload>()
            .moveToIO()
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
            .shareWhileSubscribed(Idle())

    /** ## Reduced State **/
    val TodoListDisplay =
        combine(
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
            .moveToIO()
            .distinctUntilChanged()
            .shareWhileSubscribed(listOf())

    val SearchError =
        combine(
            todoListState,
            TodoListDisplay
        ) { raw, reduced ->
            val rawDataSize =
                raw
                    .fold(
                        onSuccess = { it }
                    ) {
                        listOf()
                    }
                    .size

            if (reduced.isEmpty() && rawDataSize > 0)
                "Not found"
            else
                null
        }
            .shareWhileSubscribed("")

    val Status =
        todoListState
            .map {
                it::class.simpleName
            }
            .shareWhileSubscribed("")

    val Error =
        todoListState
            .map {
                it.fold(
                    onFailed = { e -> e.message },
                    onElse = { "" }
                )
            }
            .shareWhileSubscribed("")


    val AppliedFilters =
        combine(
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
            .shareWhileSubscribed("")

    val SearchClue =
        searchClue
            .shareWhileSubscribed("")

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