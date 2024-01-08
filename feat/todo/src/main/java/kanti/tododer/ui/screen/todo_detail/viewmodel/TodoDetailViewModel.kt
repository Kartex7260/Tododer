package kanti.tododer.ui.screen.todo_detail.viewmodel

import kanti.tododer.ui.components.todo.TodoUiState
import kanti.tododer.ui.components.todo.TodosUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

	val onDeleted: SharedFlow<OnTodoDeletedUiState>

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

		private val coroutineScope = CoroutineScope(Dispatchers.Main)

		private const val maxPop = 3
		private var currentPop = 0

		private val _emptyStack = MutableSharedFlow<Unit>()
		override val emptyStack: SharedFlow<Unit> = _emptyStack.asSharedFlow()

		private val _todoDetail = MutableStateFlow(TodoUiState(id = 1, title = "Parent todo", remark = "Test remark"))
		override val todoDetail: StateFlow<TodoUiState> = _todoDetail.asStateFlow()

		private val _todoDeleted = MutableSharedFlow<OnTodoDeletedUiState>()
		override val onDeleted: SharedFlow<OnTodoDeletedUiState> = _todoDeleted.asSharedFlow()
		
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
		}

		override fun changeTitle(title: String) {
			_todoDetail.value = _todoDetail.value.copy(
				title = title
			)
		}

		override fun changeRemark(remark: String) {
			_todoDetail.value = _todoDetail.value.copy(
				remark = remark
			)
		}

		override fun changeDoneCurrent(isDone: Boolean) {
			_todoDetail.value = _todoDetail.value.copy(
				isDone = isDone
			)
		}

		override fun changeDoneChild(todoId: Int, isDone: Boolean) {
		}

		override fun push(todoId: Int) {
		}

		override fun pop() {
			currentPop++
			if (currentPop >= maxPop) {
				coroutineScope.launch {
					_emptyStack.emit(value = Unit)
				}
			}
		}

		override fun deleteCurrent() {
			coroutineScope.launch {
				_todoDeleted.emit(OnTodoDeletedUiState.ShowMessage("Test delete"))
				delay(5000L)
				_todoDeleted.emit(OnTodoDeletedUiState.HideMessage)
			}
		}

		override fun deleteChild(todoId: Int) {
			coroutineScope.launch {
				_todoDeleted.emit(OnTodoDeletedUiState.ShowMessage("Test delete"))
				delay(5000L)
				_todoDeleted.emit(OnTodoDeletedUiState.HideMessage)
			}
		}

		override fun undoDelete() {
		}
	}
}