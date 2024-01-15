package kanti.tododer.ui.screen.todo_list.viewmodel

import android.util.Log
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.ui.components.todo.TodosData
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

	val todosDeleted: SharedFlow<List<TodoData>>

	val newTodoCreated: SharedFlow<Long>

	fun createNewTodo()

	fun changeDone(todoId: Long, isDone: Boolean)

	fun deleteTodos(todos: List<TodoData>)

	fun cancelDelete()

	fun rejectCancelChance()

	companion object : TodoListViewModel {

		private const val logTag = "TodoListViewModel"

		private val coroutineScope = CoroutineScope(Dispatchers.Default)

		private val _children = MutableStateFlow<TodoListUiState>(TodoListUiState.Success(
			plan = Plan(1, title = "Test", type = PlanType.Custom),
			children = TodosData(listOf(
				TodoData(1, "Test 1"),
				TodoData(2, "Test 2"),
				TodoData(3, "Test 3"),
				TodoData(4, "Test 4"),
				TodoData(5, "Test 5"),
				TodoData(6, "Test 6"),
				TodoData(7, "Test 7"),
				TodoData(8, "Test 8"),
				TodoData(9, "Test 9"),
				TodoData(10, "Test 10"),
				TodoData(11, "Test 11")
			))
		))
		override val currentPlan = _children.asStateFlow()

		private val _todoDeleted = MutableSharedFlow<List<TodoData>>()
		override val todosDeleted: SharedFlow<List<TodoData>> = _todoDeleted.asSharedFlow()

		private val _newTodoCreated = MutableSharedFlow<Long>()
		override val newTodoCreated: SharedFlow<Long> = _newTodoCreated.asSharedFlow()

		override fun createNewTodo() {
			coroutineScope.launch {
				_newTodoCreated.emit(0L)
			}
		}

		override fun changeDone(todoId: Long, isDone: Boolean) {
			Log.d(logTag, "changeDone(todoId: Int = $todoId, isDone: Boolean = $isDone)")
		}

		override fun deleteTodos(todos: List<TodoData>) {
			Log.d(logTag, "deleteTodo(todoIds: List<TodoData> = $todos)")
			coroutineScope.launch {
				_todoDeleted.emit(listOf(TodoData(title = "Test todo")))
			}
		}

		override fun cancelDelete() {
			Log.d(logTag, "undoDelete()")
		}

		override fun rejectCancelChance() {
			Log.d(logTag, "rejectCancelChance()")
		}
	}
}