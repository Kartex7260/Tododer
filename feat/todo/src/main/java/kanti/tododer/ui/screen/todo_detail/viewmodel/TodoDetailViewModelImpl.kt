package kanti.tododer.ui.screen.todo_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toTodoData
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModelImpl @Inject constructor(
	private val todoRepository: TodoRepository
) : ViewModel(), TodoDetailViewModel {

	private val stack: Stack<Long> = Stack()
	private val _currentTodo = MutableStateFlow(EMPTY_TODO_ID)

	private val deleteCancelManager = DeleteCancelManager<TodoData>(
		toKey = { id },
		onDelete = { todos ->
			todoRepository.delete(todos.map { it.id })
			_updateTodoChildren.value = Any()
		}
	)

	private val _emptyStack = MutableSharedFlow<Unit>()
	override val emptyStack: SharedFlow<Unit> = _emptyStack.asSharedFlow()

	override val todoDetail: StateFlow<TodoData> = _currentTodo
		.map { todoId ->
			val todo = todoRepository.getTodo(todoId)
			todo?.toTodoData()
		}
		.filterNotNull()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodoData(id = EMPTY_TODO_ID)
		)

	private val _updateTodoChildren = MutableStateFlow(Any())
	override val todoChildren: StateFlow<TodosData> = todoDetail
		.combine(_updateTodoChildren) { todo, _ -> todo }
		.map { todoData ->
			val fullId = FullId(todoData.id, FullIdType.Todo)
			todoRepository.getChildren(fullId)
		}
		.combine(deleteCancelManager.deletedValues) { children, deletedChildren ->
			children.filter { todo ->
				!deletedChildren.containsKey(todo.id)
			}
		}
		.map { children ->
			TodosData(
				todos = children.map { it.toTodoData() }
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodosData()
		)

	private val _todosDeleted = MutableSharedFlow<List<TodoData>>()
	override val todosDeleted: SharedFlow<List<TodoData>> = _todosDeleted.asSharedFlow()

	override fun createNewTodo() {
		viewModelScope.launch {
			val currentTodo = todoDetail.value
			if (currentTodo.id == EMPTY_TODO_ID)
				return@launch
			val parentFullId = FullId(currentTodo.id, FullIdType.Todo)
			val todoId = todoRepository.create(parentFullId, "", "")
			push(todoId)
			_updateTodoChildren.value = Any()
		}
	}

	override fun changeTitle(title: String) {
	}

	override fun changeRemark(remark: String) {
	}

	override fun changeDoneCurrent(isDone: Boolean) {
	}

	override fun changeDoneChild(todoId: Long, isDone: Boolean) {
	}

	override fun deleteCurrent() {
	}

	override fun deleteChildren(todos: List<TodoData>) {
		viewModelScope.launch {
			if (todos.isEmpty())
				return@launch
			deleteCancelManager.delete(todos)
			_todosDeleted.emit(todos)
		}
	}

	override fun cancelDelete() {
		viewModelScope.launch {
			deleteCancelManager.cancelDelete()
		}
	}

	override fun rejectCancelDelete() {
		viewModelScope.launch {
			deleteCancelManager.rejectCancelChance()
		}
	}

	override fun push(todoId: Long) {
		if (_currentTodo.value != EMPTY_TODO_ID)
			stack.push(todoDetail.value.id)
		viewModelScope.launch {
			_currentTodo.emit(todoId)
		}
	}

	override fun pop() {
		viewModelScope.launch {
			try {
				val current = stack.pop()
				_currentTodo.emit(current)
			} catch (ex: EmptyStackException) {
				_emptyStack.emit(Unit)
			}
		}
	}

	companion object {

		private const val EMPTY_TODO_ID = 0L
	}
}