package kanti.tododer.ui.screen.todo_list.viewmodel

import android.util.Log
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.ui.common.TodoUiState
import kanti.tododer.ui.common.TodosUiState
import kanti.tododer.ui.components.todo.TodoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface TodoListViewModel {

    val currentPlan: StateFlow<TodoListUiState>

    val todosDeleted: SharedFlow<List<TodoDeletion>>
    val blankTodoDeleted: SharedFlow<Unit>

    val goToTodo: SharedFlow<Long>

    fun updateUiState(deletedTodoId: Long?)

    fun createNewTodo(title: String, goTo: Boolean)

    fun renamePlan(newTitle: String)

    fun renameTodo(todoId: Long, newTitle: String)

    fun changeDone(todoId: Long, isDone: Boolean)

    fun deleteTodos(todos: List<TodoData>)

    fun cancelDelete()

    fun rejectCancelChance()

    fun onStop()

    companion object : TodoListViewModel {

        private const val logTag = "TodoListViewModel"

        private val coroutineScope = CoroutineScope(Dispatchers.Default)

        private val _children = MutableStateFlow<TodoListUiState>(
            TodoListUiState(
                plan = Plan(1, title = "Test", type = PlanType.Custom),
                children = TodosUiState(
                    todos = listOf(
                        TodoUiState(data = TodoData(1, "Test 1")),
                        TodoUiState(data = TodoData(2, "Test 2")),
                        TodoUiState(data = TodoData(3, "Test 3")),
                        TodoUiState(data = TodoData(4, "Test 4")),
                        TodoUiState(data = TodoData(5, "Test 5")),
                        TodoUiState(data = TodoData(6, "Test 6")),
                        TodoUiState(data = TodoData(7, "Test 7")),
                        TodoUiState(data = TodoData(8, "Test 8")),
                        TodoUiState(data = TodoData(9, "Test 9")),
                        TodoUiState(data = TodoData(10, "Test 10")),
                        TodoUiState(data = TodoData(11, "Test 11")),
                        TodoUiState(data = TodoData(12, "Test 12")),
                        TodoUiState(data = TodoData(13, "Test 13")),
                        TodoUiState(data = TodoData(14, "Test 14"))
                    )
                )
            )
        )
        override val currentPlan = _children.asStateFlow()

        private val _todoDeleted = MutableSharedFlow<List<TodoDeletion>>()
        override val todosDeleted: SharedFlow<List<TodoDeletion>> = _todoDeleted.asSharedFlow()

        override val blankTodoDeleted: SharedFlow<Unit> = MutableSharedFlow()

        private val _newTodoCreated = MutableSharedFlow<Long>()
        override val goToTodo: SharedFlow<Long> = _newTodoCreated.asSharedFlow()

        override fun updateUiState(deletedTodoId: Long?) {
        }

        override fun createNewTodo(title: String, goTo: Boolean) {
            coroutineScope.launch {
                _newTodoCreated.emit(0L)
            }
        }

        override fun renamePlan(newTitle: String) {
        }

        override fun renameTodo(todoId: Long, newTitle: String) {
        }

        override fun changeDone(todoId: Long, isDone: Boolean) {
            Log.d(logTag, "changeDone(todoId: Int = $todoId, isDone: Boolean = $isDone)")
        }

        override fun deleteTodos(todos: List<TodoData>) {
            Log.d(logTag, "deleteTodo(todoIds: List<TodoData> = $todos)")
            coroutineScope.launch {
                _todoDeleted.emit(listOf(TodoDeletion(TodoData(title = "Test todo"), false)))
            }
        }

        override fun cancelDelete() {
            Log.d(logTag, "undoDelete()")
        }

        override fun rejectCancelChance() {
            Log.d(logTag, "rejectCancelChance()")
        }

        override fun onStop() {
            Log.d(logTag, "onStop()")
        }
    }
}