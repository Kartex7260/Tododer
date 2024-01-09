package kanti.tododer.ui.screen.todo_detail.viewmodel

import android.util.Log
import kanti.tododer.ui.components.todo.TodoUiState
import kanti.tododer.ui.components.todo.TodosUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface TodoDetailViewModel {

	val emptyStack: SharedFlow<Unit>

	val todoDetail: StateFlow<TodoUiState>
	val todoChildren: StateFlow<TodosUiState>

	val onDeleted: SharedFlow<String>

	fun createNewTodo()

	fun changeTitle(title: String)

	fun changeRemark(remark: String)

	fun changeDoneCurrent(isDone: Boolean)

	fun changeDoneChild(todoId: Int, isDone: Boolean)

	fun deleteCurrent()

	fun deleteChild(todoId: Int)

	fun undoDelete()

	fun push(todoId: Int)

	fun pop()

	companion object : TodoDetailViewModel {

		private const val logTag = "TodoDetailViewModel"

		private val coroutineScope = CoroutineScope(Dispatchers.Main)

		private const val maxPop = 3
		private var currentPop = 0

		private val _emptyStack = MutableSharedFlow<Unit>()
		override val emptyStack: SharedFlow<Unit> = _emptyStack.asSharedFlow()

		private val _todoDetail = MutableStateFlow(TodoUiState(id = 1, title = "Parent todo", remark = "Test remark"))
		override val todoDetail: StateFlow<TodoUiState> = _todoDetail.asStateFlow()

		private val _todoDeleted = MutableSharedFlow<String>()
		override val onDeleted: SharedFlow<String> = _todoDeleted.asSharedFlow()
		
		private val _todoChildren = MutableStateFlow(TodosUiState(listOf(
			TodoUiState(id = 2, title = "Test 1"),
			TodoUiState(id = 3, title = "Test 2"),
			TodoUiState(id = 4, title = "Test 3"),
			TodoUiState(id = 5, title = "Test 4"),
			TodoUiState(id = 6, title = "Test 5"),
			TodoUiState(id = 7, title = "Test 6"),
			TodoUiState(id = 8, title = "Test 7"),
			TodoUiState(id = 9, title = "Test 8"),
			TodoUiState(id = 10, title = "Test 9"),
			TodoUiState(id = 11, title = "Test 10"),
			TodoUiState(id = 12, title = "Test 11"),
			TodoUiState(id = 13, title = "Test 12"),
		)))
		override val todoChildren: StateFlow<TodosUiState> = _todoChildren.asStateFlow()

		override fun createNewTodo() {
			Log.d(logTag, "createNewTodo()")
		}

		override fun changeTitle(title: String) {
			Log.d(logTag, "changeTitle(title: String = $title)")
			_todoDetail.value = _todoDetail.value.copy(
				title = title
			)
		}

		override fun changeRemark(remark: String) {
			Log.d(logTag, "changeRemark(remark: String = $remark)")
			_todoDetail.value = _todoDetail.value.copy(
				remark = remark
			)
		}

		override fun changeDoneCurrent(isDone: Boolean) {
			Log.d(logTag, "changeDoneCurrent(isDone: Boolean = $isDone)")
			_todoDetail.value = _todoDetail.value.copy(
				isDone = isDone
			)
		}

		override fun changeDoneChild(todoId: Int, isDone: Boolean) {
			Log.d(logTag, "changeDoneChild(todoId: Int = $todoId, isDone: Boolean = $isDone)")
		}

		override fun push(todoId: Int) {
			Log.d(logTag, "push(todoId: Int = $todoId)")
		}

		override fun pop() {
			Log.d(logTag, "pop()")
			currentPop++
			if (currentPop >= maxPop) {
				coroutineScope.launch {
					_emptyStack.emit(value = Unit)
				}
			}
		}

		override fun deleteCurrent() {
			Log.d(logTag, "deleteCurrent()")
			coroutineScope.launch {
				_todoDeleted.emit("Current todo")
			}
		}

		override fun deleteChild(todoId: Int) {
			Log.d(logTag, "deleteChild(todoId: Int = $todoId)")
			coroutineScope.launch {
				_todoDeleted.emit("Child todo")
			}
		}

		override fun undoDelete() {
			Log.d(logTag, "undoDelete()")
		}
	}
}