package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.tododer.common.Const
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.common.Todo
import kanti.tododer.ui.fragments.components.todo_list.TodoViewHolderManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {

	private val logTag = javaClass.simpleName

	private val _todoListLiveData = MutableLiveData<List<Todo>>()
	val todoListLiveData: LiveData<List<Todo>> = _todoListLiveData

	private val _onElementClickSharedFlow = MutableSharedFlow<Todo>()
	val onElementClickSharedFlow = _onElementClickSharedFlow.asSharedFlow()

	private val _taskIsDoneLiveData = MutableLiveData<TaskIsDoneResponse>()
	val taskIsDoneLiveData: LiveData<TaskIsDoneResponse> = _taskIsDoneLiveData

	fun setTodoList(list: List<Todo> = listOf()) {
		_todoListLiveData.value = list
	}

	fun elementClick(todo: Todo) {
		viewModelScope.launch {
			Log.d(logTag, "elementClick(Todo = $todo)")
			_onElementClickSharedFlow.emit(todo)
		}
	}

	fun taskIsDone(task: Task, done: Boolean): LiveData<UiState<Task>> {
		val callbackLiveData = MutableLiveData<UiState<Task>>()
		_taskIsDoneLiveData.value = TaskIsDoneResponse(task, done, callbackLiveData)
		return callbackLiveData
	}

}