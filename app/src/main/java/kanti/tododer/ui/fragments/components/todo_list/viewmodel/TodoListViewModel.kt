package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import android.view.ContextMenu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.ui.common.viewholder.ItemListTodoViewHolderFactory
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.SaveTodoDataRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel(), TodoListOwnerViewModel, TodoListUserViewModel {

	private val logTag = javaClass.simpleName

	override var todoViewHolderFactory: TodoViewHolder.Factory = ItemListTodoViewHolderFactory

	private val _todoListLiveData = MutableStateFlow<List<Todo>>(listOf())
	override val todoList = _todoListLiveData.asStateFlow()

	private val _elementClick = MutableSharedFlow<Todo>()
	override val elementClick = _elementClick.asSharedFlow()

	private val _taskIsDone = MutableSharedFlow<SaveTodoDataRequest<Boolean>>()
	override val taskIsDone = _taskIsDone.asSharedFlow()

	private val _planProgressRequest = MutableSharedFlow<PlanProgressRequest>()
	override val planProgressRequest = _planProgressRequest.asSharedFlow()

	private val _deleteTodo = MutableSharedFlow<DeleteTodoRequest>()
	override val deleteTodo = _deleteTodo.asSharedFlow()

	private val _todoItemCreateContextMenu = MutableSharedFlow<TodoItemCreateContextMenuRequest>()
	override val todoItemCreateContextMenu = _todoItemCreateContextMenu.asSharedFlow()

	private val _todoArchived = MutableSharedFlow<Todo>()
	override val todoArchived = _todoArchived.asSharedFlow()

	override fun deleteTodo(todo: Todo) {
		viewModelScope.launch {
			_deleteTodo.emit(
				DeleteTodoRequest(
					todo = todo
				)
			)
		}
	}

	override fun sendTodoList(list: List<Todo>) {
		_todoListLiveData.value = list
	}

	override fun elementClick(todo: Todo) {
		viewModelScope.launch {
			_elementClick.emit(todo)
		}
	}

	override fun taskIsDone(task: Task, done: Boolean) {
		viewModelScope.launch {
			_taskIsDone.emit(
				SaveTodoDataRequest(task, done)
			)
		}
	}

	override fun progressRequest(plan: Plan): LiveData<Float> {
		val callback = MutableLiveData<Float>()
		viewModelScope.launch {
			_planProgressRequest.emit(
				PlanProgressRequest(
					plan,
					callback
				)
			)
		}
		return callback
	}

	override fun todoItemCreateContextMenu(todo: Todo, menu: ContextMenu) {
		viewModelScope.launch {
			_todoItemCreateContextMenu.emit(
				TodoItemCreateContextMenuRequest(
					todo = todo,
					contextMenu = menu
				)
			)
		}
	}

	override fun removeTodoView(todo: Todo) {
		viewModelScope.launch {
			_todoArchived.emit(todo)
		}
	}

}