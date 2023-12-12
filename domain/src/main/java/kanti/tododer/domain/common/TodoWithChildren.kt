package kanti.tododer.domain.common

import kanti.tododer.data.model.common.Todo

data class TodoWithChildren(
	val todo: Todo,
	val childTasks: List<Todo> = listOf()
)
