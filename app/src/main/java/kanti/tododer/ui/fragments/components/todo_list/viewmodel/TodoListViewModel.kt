package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.TaskIsDoneRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {

	private val logTag = javaClass.simpleName

	private val _todoListLiveData = MutableStateFlow<List<Todo>>(listOf())
	val todoList = _todoListLiveData.asStateFlow()

	private val _onElementClick = MutableSharedFlow<Todo>()
	val onElementClick = _onElementClick.asSharedFlow()

	private val _onTaskIsDone = MutableSharedFlow<TaskIsDoneRequest>()
	val onTaskIsDone = _onTaskIsDone.asSharedFlow()

	private val _onPlanProgressRequest = MutableSharedFlow<PlanProgressRequest>()
	val onPlanProgressRequest = _onPlanProgressRequest.asSharedFlow()

	fun sendTodoList(list: List<Todo> = listOf()) {
		_todoListLiveData.value = list
	}

	fun elementClick(todo: Todo) {
		viewModelScope.launch {
			_onElementClick.emit(todo)
		}
	}

	fun taskIsDone(task: Task, done: Boolean): LiveData<UiState<Task>> {
		val callbackLiveData = MutableLiveData<UiState<Task>>()
		viewModelScope.launch {
			_onTaskIsDone.emit(
				TaskIsDoneRequest(task, done, callbackLiveData)
			)
		}
		return callbackLiveData
	}

	fun progressRequest(plan: Plan): LiveData<Float> {
		val callback = MutableLiveData<Float>()
		viewModelScope.launch {
			_onPlanProgressRequest.emit(
				PlanProgressRequest(
					plan,
					callback
				)
			)
		}
		return callback
	}

}