package kanti.tododer.ui.screen.todo_list.viewmodel

import android.util.Log
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.ui.components.todo.TodosUiState
import kanti.tododer.ui.components.todo.TodoUiState
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

	val todoDeleted: SharedFlow<String>

	fun changeDone(todoId: Int, isDone: Boolean)

	fun deleteTodo(todoId: Int)

	fun undoDelete()

	companion object : TodoListViewModel {

		private const val logTag = "TodoListViewModel"

		private val coroutineScope = CoroutineScope(Dispatchers.Default)

		private val _children = MutableStateFlow<TodoListUiState>(TodoListUiState.Success(
			plan = Plan(1, title = "Test", type = PlanType.Custom),
			children = TodosUiState(listOf(
				TodoUiState(1, "Test 1"),
				TodoUiState(2, "Test 2"),
				TodoUiState(3, "Test 3"),
				TodoUiState(4, "Test 4"),
				TodoUiState(5, "Test 5"),
				TodoUiState(6, "Test 6"),
				TodoUiState(7, "Test 7"),
				TodoUiState(8, "Test 8"),
				TodoUiState(9, "Test 9"),
				TodoUiState(10, "Test 10"),
				TodoUiState(11, "Test 11")
			))
		))
		override val currentPlan = _children.asStateFlow()

		private val _todoDeleted = MutableSharedFlow<String>()
		override val todoDeleted: SharedFlow<String> = _todoDeleted.asSharedFlow()

		override fun changeDone(todoId: Int, isDone: Boolean) {
			Log.d(logTag, "changeDone(todoId: Int = $todoId, isDone: Boolean = $isDone)")
		}

		override fun deleteTodo(todoId: Int) {
			Log.d(logTag, "deleteTodo(todoId: Int = $todoId)")
			coroutineScope.launch {
				_todoDeleted.emit("Test todo")
			}
		}

		override fun undoDelete() {
			Log.d(logTag, "undoDelete()")
		}
	}
}