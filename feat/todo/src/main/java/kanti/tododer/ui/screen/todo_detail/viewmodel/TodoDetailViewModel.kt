package kanti.tododer.ui.screen.todo_detail.viewmodel

import android.util.Log
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
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

	val todoDetail: StateFlow<TodoData>
	val todoChildren: StateFlow<TodosData>

	val todosDeleted: SharedFlow<List<TodoData>>

	fun createNewTodo()

	fun changeTitle(title: String)

	fun changeRemark(remark: String)

	fun changeDoneCurrent(isDone: Boolean)

	fun changeDoneChild(todoId: Long, isDone: Boolean)

	fun deleteCurrent()

	fun deleteChildren(todos: List<TodoData>)

	fun cancelDelete()

	fun rejectCancelDelete()

	fun push(todoId: Long)

	fun pop()

	companion object : TodoDetailViewModel {

		private const val logTag = "TodoDetailViewModel"

		private val coroutineScope = CoroutineScope(Dispatchers.Main)

		private const val maxPop = 3
		private var currentPop = 0

		private val _emptyStack = MutableSharedFlow<Unit>()
		override val emptyStack: SharedFlow<Unit> = _emptyStack.asSharedFlow()

		private val _todoDetail = MutableStateFlow(TodoData())
		override val todoDetail: StateFlow<TodoData> = _todoDetail.asStateFlow()

		private val _todoDeleted = MutableSharedFlow<List<TodoData>>()
		override val todosDeleted: SharedFlow<List<TodoData>> = _todoDeleted.asSharedFlow()
		
		private val _todoChildren = MutableStateFlow(TodosData(listOf(
			TodoData(id = 2, title = "Test 1"),
			TodoData(id = 3, title = "Test 2"),
			TodoData(id = 4, title = "Test 3"),
			TodoData(id = 5, title = "Test 4"),
			TodoData(id = 6, title = "Test 5"),
			TodoData(id = 7, title = "Test 6"),
			TodoData(id = 8, title = "Test 7"),
			TodoData(id = 9, title = "Test 8"),
			TodoData(id = 10, title = "Test 9"),
			TodoData(id = 11, title = "Test 10"),
			TodoData(id = 12, title = "Test 11"),
			TodoData(id = 13, title = "Test 12"),
		)))
		override val todoChildren: StateFlow<TodosData> = _todoChildren.asStateFlow()

		override fun createNewTodo() {
			Log.d(logTag, "createNewTodo()")
		}

		override fun changeTitle(title: String) {
			Log.d(logTag, "changeTitle(title: String = $title)")
			_todoDetail.value = _todoDetail.value.copy()
		}

		override fun changeRemark(remark: String) {
			Log.d(logTag, "changeRemark(remark: String = $remark)")
			_todoDetail.value = _todoDetail.value.copy()
		}

		override fun changeDoneCurrent(isDone: Boolean) {
			Log.d(logTag, "changeDoneCurrent(isDone: Boolean = $isDone)")
			_todoDetail.value = _todoDetail.value.copy()
		}

		override fun changeDoneChild(todoId: Long, isDone: Boolean) {
			Log.d(logTag, "changeDoneChild(todoId: Int = $todoId, isDone: Boolean = $isDone)")
		}

		override fun push(todoId: Long) {
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
				_todoDeleted.emit(listOf(TodoData(title = "Current todo")))
			}
		}

		override fun deleteChildren(todos: List<TodoData>) {
			Log.d(logTag, "deleteChild(todoId: Int = $todos)")
			coroutineScope.launch {
				_todoDeleted.emit(listOf(TodoData(title = "Child todo")))
			}
		}

		override fun cancelDelete() {
			Log.d(logTag, "undoDelete()")
		}

		override fun rejectCancelDelete() {
			Log.d(logTag, "rejectCancelDelete()")
		}
	}
}