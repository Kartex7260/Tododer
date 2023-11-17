package kanti.tododer.domain.common

import kanti.tododer.data.model.common.Todo

data class TodoWithChildren(
	val todo: Todo? = null,
	val children: List<Todo> = listOf()
)
