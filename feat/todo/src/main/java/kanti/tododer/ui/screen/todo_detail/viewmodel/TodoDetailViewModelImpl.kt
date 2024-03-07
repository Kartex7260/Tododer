package kanti.tododer.ui.screen.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.domain.todo.delete.DeleteBlankTodoWithFlow
import kanti.tododer.ui.common.GroupUiState
import kanti.tododer.ui.common.TodoDataWithGroup
import kanti.tododer.ui.common.TodosUiState
import kanti.tododer.ui.common.toData
import kanti.tododer.ui.common.toUiState
import kanti.tododer.ui.components.grouping.GroupExpandingController
import kanti.tododer.ui.components.selection.SelectionController
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoDeletion
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModelImpl @Inject constructor(
	private val todoRepository: TodoRepository,
	private val deleteBlankTodoWithFlow: DeleteBlankTodoWithFlow,
	private val selectionController: SelectionController,
	private val groupExpandingController: GroupExpandingController
) : ViewModel(), TodoDetailViewModel {

	private val logTag = "TodoDetailViewModelImpl"

	private val _currentTodo = MutableStateFlow(EMPTY_TODO_ID)

	private val deleteCancelManager = DeleteCancelManager<TodoDeletion>(
		toKey = { todoData.id },
		onDelete = { todos ->
			withContext(NonCancellable) {
				todoRepository.delete(todos.map { it.todoData.id })
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
			todo?.toData()
		}
		.filterNotNull()
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodoData(id = EMPTY_TODO_ID)
		)

	private val _updateTodoChildren = MutableStateFlow(Any())
	override val todoChildren: StateFlow<TodosUiState> = todoDetail
		.combine(_updateTodoChildren) { todo, _ -> todo }
		.map { todoData ->
			Log.d(logTag, "TodoRepository.getChildren(${todoData.id})")
			val fullId = FullId(todoData.id, FullIdType.Todo)
			todoRepository.getChildren(fullId)
		}
		.run {
			combine(
				flow = this,
				flow2 = deleteCancelManager.deletedValues,
				flow3 = selectionController.selectionState
			) { children, deletedChildren, selectionState ->
				TodosUiState(
					selection = selectionState.selection,
					groups = children.groupBy { it.group }.entries.asFlow()
						.map { groupWithTodos ->
							GroupUiState(
								name = groupWithTodos.key,
								expand = groupExpandingController.visit(groupWithTodos.key),
								todos = groupWithTodos.value
									.map { todo ->
										todo.toUiState(
											selected = selectionState.selected.contains(todo.id),
											visible = !deletedChildren.containsKey(todo.id)
										)
									}
							)
						}
						.toList()
						.sortedWith(comparator = GroupUiState.COMPARATOR)
				)
			}
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodosUiState()
		)

	private val _childrenTodosDeleted = MutableSharedFlow<List<TodoDeletion>>()
	override val childrenTodosDeleted: SharedFlow<List<TodoDeletion>> =
		_childrenTodosDeleted.asSharedFlow()

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

	private val _onExit = MutableSharedFlow<TodoData?>()
	override val onExit: SharedFlow<TodoData?> = _onExit.asSharedFlow()

	private val _groupSelected = MutableSharedFlow<List<TodoDataWithGroup>>()
	override val groupSelected: SharedFlow<List<TodoDataWithGroup>> = _groupSelected.asSharedFlow()

	override fun show(todoId: Long) {
		_currentTodo.value = todoId
	}

	override fun reshow(todoId: Long?) {
		_updateTodoDetail.value = Any()
		_updateTodoChildren.value = Any()

		if (todoId == null) return

		viewModelScope.launch {
			val todo = todoRepository.getTodo(todoId) ?: return@launch
			val todoDeletion = listOf(TodoDeletion(todo.toData(), true))
			deleteCancelManager.delete(todoDeletion)
			_childrenTodosDeleted.emit(todoDeletion)
		}
	}

	override fun createNewTodo(title: String, goTo: Boolean) {
		viewModelScope.launch {
			val currentTodo = todoDetail.value
			if (currentTodo.id == EMPTY_TODO_ID)
				return@launch
			val parentFullId = FullId(currentTodo.id, FullIdType.Todo)
			val todoId = todoRepository.create(parentFullId, title, "")
			if (goTo) {
				_toNext.emit(todoId)
			}
			_updateTodoChildren.value = Any()
		}
	}

	override fun setGroup(todoIds: List<Long>, group: String?) {
		viewModelScope.launch {
			todoRepository.setGroup(todoIds, group)
			_updateTodoChildren.value = Any()
		}
	}

	override fun setGroupExpand(group: String?, expand: Boolean) {
		groupExpandingController.setExpand(group, expand)
		_updateTodoChildren.value = Any()
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

	override fun changeGroupDone(group: String?, isDone: Boolean) {
		viewModelScope.launch {
			val fullId = FullId(todoDetail.value.id, FullIdType.Todo)
			todoRepository.changeGroupDone(fullId, group, isDone)
			_updateTodoChildren.value = Any()
		}
	}

	override fun deleteCurrent() {
		viewModelScope.launch(NonCancellable) {
			val currentTodoId = _currentTodo.value
			if (currentTodoId == EMPTY_TODO_ID)
				return@launch

			val todoData = todoDetail.value
			_onExit.emit(todoData)
		}
	}

	override fun deleteChildren(todos: List<TodoData>) {
		viewModelScope.launch {
			if (todos.isEmpty())
				return@launch
			val todoDeletions = todos.map { TodoDeletion(it, false) }
			deleteCancelManager.delete(todoDeletions)
			_childrenTodosDeleted.emit(todoDeletions)
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

	override fun switchSelection() {
		selectionController.switchSelection()
	}

	override fun selection(todoId: Long) {
		selectionController.selection = true
		selectionController.setSelect(todoId, true)
	}

	override fun selectionOff(): Boolean {
		if (selectionController.selection) {
			selectionController.clear()
			return true
		}
		return false
	}

	override fun setSelect(todoId: Long, selected: Boolean) {
		selectionController.setSelect(todoId, selected)
	}

	override fun setSelect(group: String?, selected: Boolean) {
		viewModelScope.launch {
			val groupUiState = todoChildren.value.groups
				.firstOrNull { it.name == group } ?: return@launch
			selectionController.setSelect(
				ids = groupUiState.todos.map { it.data.id },
				select = selected
			)
		}
	}

	override fun groupSelected() {
		viewModelScope.launch {
			val selected = selectionController.selected
			val todos = todoChildren.value.groups.asSequence()
				.flatMap { groupUiState ->
					groupUiState.todos.map { todoUiState ->
						TodoDataWithGroup(todoData = todoUiState.data, group = groupUiState.name)
					}
				}
				.filter { selected.contains(it.todoData.id) }
			_groupSelected.emit(todos.toList())
		}
	}

	override fun changeDoneSelected() {
		viewModelScope.launch {
			val selected = selectionController.selected
			if (selected.isEmpty())
				return@launch
			val children = todoChildren.value.groups
				.flatMap { it.todos }
				.filter { selected.contains(it.data.id) }
				.map { it.data }
			val totalDone = children.fold(true) { acc, todoData -> acc and todoData.isDone }
			todoRepository.changeDone(
				todoIds = selected,
				isDone = !totalDone
			)
			_updateTodoChildren.value = Any()
		}
	}

	override fun deleteSelected() {
		viewModelScope.launch {
			val selected = selectionController.selected
			if (selected.isEmpty())
				return@launch
			selectionController.clear()
			val children = todoChildren.value.groups
				.flatMap { it.todos }
				.filter { selected.contains(it.data.id) }
				.map { it.data }
			deleteChildren(children)
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