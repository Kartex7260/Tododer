package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.Task
import kanti.tododer.ui.fragments.components.common.PlanProgressRequest
import kanti.tododer.ui.fragments.components.common.SaveTodoDataRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.IllegalStateException

class TodoDataViewModel : ViewModel(), TodoDataOwnerViewModel, TodoDataUserViewModel {

	private var todoDataSaveObserver: TodoDataSaveLifecycleObserver? = null
	private var currentTodo: Todo? = null
		set(value) {
			todoDataSaveObserver?.todo = value
			field = value
		}
	private val _todoElement = MutableStateFlow(TodoDataUiState())
	override val todoElement = _todoElement.asStateFlow()

	private val _updateStateView = MutableSharedFlow<Unit>()
	override val updateStateView = _updateStateView.asSharedFlow()

	private val _taskIsDone = MutableSharedFlow<SaveTodoDataRequest<Boolean>>(replay = 1)
	override val taskIsDone = _taskIsDone.asSharedFlow()

	private val _planProgressRequest = MutableSharedFlow<PlanProgressRequest>(replay = 1)
	override val planProgressRequest = _planProgressRequest.asSharedFlow()

	private val _saveNewTitle = MutableSharedFlow<SaveTodoDataRequest<String>>()
	override val saveNewTitle = _saveNewTitle.asSharedFlow()

	private val _saveNewRemark = MutableSharedFlow<SaveTodoDataRequest<String>>()
	override val saveNewRemark = _saveNewRemark.asSharedFlow()

	override fun saveNewTitle(title: String) {
		viewModelScope.launch {
			_saveNewTitle.emit(
				SaveTodoDataRequest(
					todo = requireCurrentTodo(),
					data = title
				)
			)
		}
	}

	override fun saveNewRemark(remark: String) {
		viewModelScope.launch {
			_saveNewRemark.emit(
				SaveTodoDataRequest(
					todo = requireCurrentTodo(),
					data = remark
				)
			)
		}
	}

	override fun sendTodo(todoElement: Todo?) {
		currentTodo = todoElement
		_todoElement.value = TodoDataUiState(
			todoElement
		)
	}

	override fun taskIsDone(task: BaseTask, done: Boolean) {
		viewModelScope.launch {
			_taskIsDone.emit(
				SaveTodoDataRequest(task, done)
			)
		}
	}

	override fun planProgressRequest(plan: BasePlan): LiveData<Float> {
		val callback = MutableLiveData<Float>()
		viewModelScope.launch {
			_planProgressRequest.emit(PlanProgressRequest(
				plan,
				callback
			))
		}
		return callback
	}

	override fun updateStateView() {
		viewModelScope.launch {
			_updateStateView.emit(Unit)
		}
	}

	override fun setTodoDataSaveObserver(lifecycleOwner: LifecycleOwner, todoSavable: TodoSavable) {
		todoDataSaveObserver = TodoDataSaveLifecycleObserver(lifecycleOwner, todoSavable)
	}

	private fun requireCurrentTodo() = currentTodo
		?: throw IllegalStateException("Current todo is null!")

}