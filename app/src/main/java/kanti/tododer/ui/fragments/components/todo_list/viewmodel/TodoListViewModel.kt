package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import android.view.ContextMenu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.common.viewholder.ItemListTodoViewHolderFactory
import kanti.tododer.ui.common.viewholder.TodoViewHolder
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.SaveTodoDataRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {

	private val logTag = javaClass.simpleName

	var todoViewHolderFactory: TodoViewHolder.Factory = ItemListTodoViewHolderFactory

	private val _todoListLiveData = MutableStateFlow<List<Todo>>(listOf())
	val todoList = _todoListLiveData.asStateFlow()

	private val _elementClick = MutableSharedFlow<Todo>()
	val onElementClick = _elementClick.asSharedFlow()

	private val _taskIsDone = MutableSharedFlow<SaveTodoDataRequest<Boolean>>()
	val onTaskIsDone = _taskIsDone.asSharedFlow()

	private val _planProgressRequest = MutableSharedFlow<PlanProgressRequest>()
	val onPlanProgressRequest = _planProgressRequest.asSharedFlow()

	private val _deleteTodo = MutableSharedFlow<DeleteTodoRequest>()
	val onDeleteTodo = _deleteTodo.asSharedFlow()

	private val _todoItemCreateContextMenu = MutableSharedFlow<TodoItemCreateContextMenuRequest>()
	val onTodoItemCreateContextMenu = _todoItemCreateContextMenu.asSharedFlow()

	fun deleteTodo(todo: Todo) {
		viewModelScope.launch {
			_deleteTodo.emit(
				DeleteTodoRequest(
					todo = todo
				)
			)
		}
	}

	fun sendTodoList(list: List<Todo> = listOf()) {
		_todoListLiveData.value = list
	}

	fun elementClick(todo: Todo) {
		viewModelScope.launch {
			_elementClick.emit(todo)
		}
	}

	fun taskIsDone(task: Task, done: Boolean) {
		viewModelScope.launch {
			_taskIsDone.emit(
				SaveTodoDataRequest(task, done)
			)
		}
	}

	fun progressRequest(plan: Plan): LiveData<Float> {
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

	fun todoItemCreateContextMenu(todo: Todo, menu: ContextMenu) {
		viewModelScope.launch {
			_todoItemCreateContextMenu.emit(
				TodoItemCreateContextMenuRequest(
					todo = todo,
					contextMenu = menu
				)
			)
		}
	}

}