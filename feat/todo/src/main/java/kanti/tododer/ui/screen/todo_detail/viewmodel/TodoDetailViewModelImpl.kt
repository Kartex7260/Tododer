package kanti.tododer.ui.screen.todo_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toTodoData
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
	private val _currentTodo = MutableSharedFlow<Long>()

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
			initialValue = TodoData()
		)
	override val todoChildren: StateFlow<TodosData>
		get() = MutableStateFlow(TodosData())
	override val onDeleted: SharedFlow<String>
		get() = MutableSharedFlow()

	override fun createNewTodo() {
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

	override fun deleteChild(todoId: Long) {
	}

	override fun undoDelete() {
	}

	override fun push(todoId: Long) {
		if (todoDetail.value.id != 0L)
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
}