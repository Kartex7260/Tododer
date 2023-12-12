package kanti.tododer.domain.common

import kanti.tododer.data.model.common.Todo

data class TodoWithChildren(
	val todo: kanti.tododer.data.model.common.Todo,
	val childTasks: List<kanti.tododer.data.model.common.Todo> = listOf()
)
