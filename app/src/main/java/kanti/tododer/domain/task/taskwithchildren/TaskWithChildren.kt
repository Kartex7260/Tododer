package kanti.tododer.domain.task.taskwithchildren

import kanti.tododer.data.model.task.Task

data class TaskWithChildren(
	val task: Task? = null,
	val childTasks: List<Task> = listOf()
)
