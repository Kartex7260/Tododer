package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kanti.tododer.data.model.task.Task
import kanti.tododer.ui.state.TodoElement

class TodoListViewModel : ViewModel() {

	private val _todoListLiveData = MutableLiveData<List<TodoElement>>()
	val todoListLiveData: LiveData<List<TodoElement>> = _todoListLiveData

	private val _onElementClickLiveData = MutableLiveData<TodoElement>()
	val onElementClickLiveData: LiveData<TodoElement> = _onElementClickLiveData

	private val _taskDoneLiveData = MutableLiveData<TaskDone>()
	val taskDoneLiveData: LiveData<TaskDone> = _taskDoneLiveData

	fun setTodoList(list: List<TodoElement> = listOf()) {
		_todoListLiveData.value = list
	}

	fun elementClick(todo: TodoElement) {
		_onElementClickLiveData.value = todo
	}

	fun taskIsDone(task: Task, done: Boolean) {
		_taskDoneLiveData.value = TaskDone(task, done)
	}

}