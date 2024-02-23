package kanti.tododer.ui.screen.todo_detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.domain.todo.delete.DeleteBlankTodoWithFlow
import kanti.tododer.ui.common.TodosUiState
import kanti.tododer.ui.common.toData
import kanti.tododer.ui.common.toUiState
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
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
        .combine(deleteCancelManager.deletedValues) { children, deletedChildren ->
            TodosUiState(
                todos = children.map { todo ->
                    todo.toUiState(visible = !deletedChildren.containsKey(todo.id))
                }
            )
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