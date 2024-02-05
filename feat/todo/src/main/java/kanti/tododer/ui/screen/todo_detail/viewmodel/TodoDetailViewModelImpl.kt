package kanti.tododer.ui.screen.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toTodoData
import kanti.tododer.domain.todo.delete.DeleteBlankTodoWithFlow
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModelImpl @Inject constructor(
	private val todoRepository: TodoRepository,
	private val deleteBlankTodoWithFlow: DeleteBlankTodoWithFlow
) : ViewModel(), TodoDetailViewModel {

	private val logTag = "TodoDetailViewModelImpl"

	private val _currentTodo = MutableStateFlow(EMPTY_TODO_ID)

	private val deleteCancelManager = DeleteCancelManager<TodoData>(
		toKey = { id },
		onDelete = { todos ->
			withContext(NonCancellable) {
				todoRepository.delete(todos.map { it.id })
			}
			_updateTodoChildren.value = Any()
		}
	)

	private val _updateTodoDetail = MutableStateFlow(Any())
	override val todoDetail: StateFlow<TodoData> = _currentTodo
		.combine(_updateTodoDetail) { currentTodo, _ -> currentTodo }
		.map { todoId ->
			Log.d(logTag, "TodoRepository.getTodo($todoId)")
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
			Log.d(logTag, "TodoRepository.getChildren(${todoData.id})")
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

	private val _childrenTodosDeleted = MutableSharedFlow<List<TodoData>>()
	override val childrenTodosDeleted: SharedFlow<List<TodoData>> = _childrenTodosDeleted.asSharedFlow()

	private val _currentTodoDeleted = MutableSharedFlow<TodoData>()
	override val currentTodoDeleted: SharedFlow<TodoData> = _currentTodoDeleted.asSharedFlow()

	override val blankTodoDeleted: SharedFlow<Unit> = deleteBlankTodoWithFlow.blankTodoDeleted
		.apply {
			viewModelScope.launch {
				collectLatest {
					_updateTodoChildren.value = Any()
				}
			}
		}

	private val _toNext = MutableSharedFlow<Long>()
	override val toNext: SharedFlow<Long> = _toNext.asSharedFlow()

	private val _onExit = MutableSharedFlow<Unit>()
	override val onExit: SharedFlow<Unit> = _onExit.asSharedFlow()

	override fun show(todoId: Long) {
		_currentTodo.value = todoId
	}

	override fun reshow() {
		_updateTodoDetail.value = Any()
		_updateTodoChildren.value = Any()
	}

	override fun createNewTodo() {
		viewModelScope.launch {
			val currentTodo = todoDetail.value
			if (currentTodo.id == EMPTY_TODO_ID)
				return@launch
			val parentFullId = FullId(currentTodo.id, FullIdType.Todo)
			val todoId = todoRepository.create(parentFullId, "", "")
			_toNext.emit(todoId)
			_updateTodoChildren.value = Any()
		}
	}

	override fun renameTodo(todoId: Long, newTitle: String) {
		viewModelScope.launch {
			todoRepository.updateTitle(todoId, newTitle)
			_updateTodoChildren.value = Any()
		}
	}

	override fun changeTitle(title: String) {
		viewModelScope.launch(NonCancellable) {
			val todoId = _currentTodo.value
			if (todoId == EMPTY_TODO_ID)
				return@launch
			todoRepository.updateTitle(todoId, title)
		}
	}

	override fun requireTodoTitle(): Flow<String> {
		return flow {
			val todo = todoRepository.getTodo(_currentTodo.value) ?: return@flow
			emit(todo.title)
		}
	}

	override fun changeRemark(remark: String) {
		viewModelScope.launch(NonCancellable) {
			val todoId = _currentTodo.value
			if (todoId == EMPTY_TODO_ID)
				return@launch
			todoRepository.updateRemark(todoId, remark)
		}
	}

	override fun changeDoneCurrent(isDone: Boolean) {
		viewModelScope.launch {
			if (_currentTodo.value == EMPTY_TODO_ID)
				return@launch
			todoRepository.changeDone(_currentTodo.value, isDone)
			_updateTodoDetail.value = Any()
		}
	}

	override fun changeDoneChild(todoId: Long, isDone: Boolean) {
		viewModelScope.launch {
			todoRepository.changeDone(todoId, isDone)
			_updateTodoChildren.value = Any()
		}
	}

	override fun deleteCurrent() {
		viewModelScope.launch(NonCancellable) {
			val currentTodoId = _currentTodo.value
			if (currentTodoId == EMPTY_TODO_ID)
				return@launch

			val todoData = todoDetail.value
			_onExit.emit(Unit)
			deleteCancelManager.delete(listOf(todoData))
			_currentTodoDeleted.emit(todoData)
		}
	}

	override fun cancelDeleteCurrent() {
		viewModelScope.launch {
			val currentDeletedMap = deleteCancelManager.deletedValues.value
			val currentDeleted = currentDeletedMap.values.firstOrNull() ?: return@launch
			deleteCancelManager.cancelDelete()
			_toNext.emit(currentDeleted.id)
		}
	}

	override fun deleteChildren(todos: List<TodoData>) {
		viewModelScope.launch {
			if (todos.isEmpty())
				return@launch
			deleteCancelManager.delete(todos)
			_childrenTodosDeleted.emit(todos)
		}
	}

	override fun cancelDeleteChildren() {
		viewModelScope.launch {
			deleteCancelManager.cancelDelete()
		}
	}

	override fun rejectCancelDelete() {
		viewModelScope.launch {
			deleteCancelManager.rejectCancelChance()
		}
	}

	override fun onStop() {
		rejectCancelDelete()
		viewModelScope.launch(NonCancellable) {
			deleteBlankTodoWithFlow(_currentTodo.value)
		}
	}

	companion object {

		private const val EMPTY_TODO_ID = 0L
	}
}