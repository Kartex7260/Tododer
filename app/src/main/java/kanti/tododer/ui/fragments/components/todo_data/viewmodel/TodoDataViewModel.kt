package kanti.tododer.ui.fragments.components.todo_data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.Task
import kanti.tododer.ui.fragments.components.todo_list.viewmodel.TaskIsDoneResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoDataViewModel : ViewModel() {

	private val _todoElement = MutableStateFlow(TodoDataUiState())
	val todoElement = _todoElement.asStateFlow()

	private val _taskIsDone = MutableSharedFlow<TaskIsDoneResponse>(replay = 1)
	val onTaskIsDone = _taskIsDone.asSharedFlow()

	fun sendTodo(todoElement: Todo? = null) {
		_todoElement.value = TodoDataUiState(
			todoElement
		)
	}

	fun taskIsDone(task: Task, done: Boolean): LiveData<UiState<Task>> {
		val callback = MutableLiveData<UiState<Task>>()
		viewModelScope.launch {
			_taskIsDone.emit(
				TaskIsDoneResponse(
					task,
					done,
					callback
				)
			)
		}
		return callback
	}

}