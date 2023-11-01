package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.TaskIsDoneRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.IllegalStateException

class TodoDataViewModel : ViewModel() {

	private var currentTodo: Todo? = null
	private val _todoElement = MutableStateFlow(TodoDataUiState())
	val todoElement = _todoElement.asStateFlow()

	private val _updateStateView = MutableSharedFlow<Unit>()
	val updateStateView = _updateStateView.asSharedFlow()

	private val _taskIsDone = MutableSharedFlow<SaveTodoDataRequest<Boolean>>(replay = 1)
	val onTaskIsDone = _taskIsDone.asSharedFlow()

	private val _planProgressRequest = MutableSharedFlow<PlanProgressRequest>(replay = 1)
	val onPlanProgressRequest = _planProgressRequest.asSharedFlow()

	private val _saveNewTitle = MutableSharedFlow<SaveTodoDataRequest<String>>()
	val onSaveNewTitle = _saveNewTitle.asSharedFlow()

	private val _saveNewRemark = MutableSharedFlow<SaveTodoDataRequest<String>>()
	val onSaveNewRemark = _saveNewRemark.asSharedFlow()

	fun saveNewTitle(title: String) {
		viewModelScope.launch {
			_saveNewTitle.emit(
				SaveTodoDataRequest(
					todo = requireCurrentTodo(),
					data = title
				)
			)
		}
	}

	fun saveNewRemark(remark: String) {
		viewModelScope.launch {
			_saveNewRemark.emit(
				SaveTodoDataRequest(
					todo = requireCurrentTodo(),
					data = remark
				)
			)
		}
	}

	fun sendTodo(todoElement: Todo? = null) {
		currentTodo = todoElement
		_todoElement.value = TodoDataUiState(
			todoElement
		)
	}

	fun taskIsDone(task: Task, done: Boolean): LiveData<UiState<Task>> {
		val callback = MutableLiveData<UiState<Task>>()
		viewModelScope.launch {
			_taskIsDone.emit(
				TaskIsDoneRequest(
					task,
					done,
					callback
				)
			)
		}
		return callback
	}

	fun planProgressRequest(plan: Plan): LiveData<Float> {
		val callback = MutableLiveData<Float>()
		viewModelScope.launch {
			_planProgressRequest.emit(PlanProgressRequest(
				plan,
				callback
			))
		}
		return callback
	}

	fun updateStateView() {
		viewModelScope.launch {
			_updateStateView.emit(Unit)
		}
	}

	private fun requireCurrentTodo() = currentTodo
		?: throw IllegalStateException("Current todo is null!")

}