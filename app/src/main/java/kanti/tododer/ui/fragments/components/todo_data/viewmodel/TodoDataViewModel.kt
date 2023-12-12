package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.task.Task
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.SaveTodoDataRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.IllegalStateException

class TodoDataViewModel : ViewModel() {

	private var todoDataSaveObserver: TodoDataSaveLifecycleObserver? = null
	private var currentTodo: kanti.tododer.data.model.common.Todo? = null
		set(value) {
			todoDataSaveObserver?.todo = value
			field = value
		}
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

	fun sendTodo(todoElement: kanti.tododer.data.model.common.Todo? = null) {
		currentTodo = todoElement
		_todoElement.value = TodoDataUiState(
			todoElement
		)
	}

	fun taskIsDone(task: kanti.tododer.data.task.Task, done: Boolean) {
		viewModelScope.launch {
			_taskIsDone.emit(
				SaveTodoDataRequest(task, done)
			)
		}
	}

	fun planProgressRequest(plan: kanti.tododer.data.model.plan.Plan): SharedFlow<GetRepositoryResult<Float>> {
		val callback = MutableSharedFlow<GetRepositoryResult<Float>>()
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

	fun setTodoDataSaveObserver(lifecycleOwner: LifecycleOwner, todoSavable: TodoSavable) {
		todoDataSaveObserver = TodoDataSaveLifecycleObserver(lifecycleOwner, todoSavable)
	}

	private fun requireCurrentTodo() = currentTodo
		?: throw IllegalStateException("Current todo is null!")

}