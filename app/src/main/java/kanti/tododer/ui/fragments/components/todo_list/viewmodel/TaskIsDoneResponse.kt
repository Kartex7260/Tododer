package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.task.Task

data class TaskIsDoneResponse(
	val task: Task,
	val done: Boolean,
	val callback: MutableLiveData<UiState<Task>>
)
