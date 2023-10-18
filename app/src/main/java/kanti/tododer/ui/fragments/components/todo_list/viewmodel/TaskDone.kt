package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import kanti.tododer.data.model.task.Task

data class TaskDone(
	val task: Task,
	val done: Boolean
)
