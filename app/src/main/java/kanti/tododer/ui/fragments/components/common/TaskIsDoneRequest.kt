package kanti.tododer.ui.fragments.components.common

import androidx.lifecycle.MutableLiveData
import kanti.tododer.data.common.UiState
import kanti.tododer.data.model.task.Task

data class TaskIsDoneRequest(
	val task: Task,
	val done: Boolean,
	val callback: MutableLiveData<UiState<Task>>
)
