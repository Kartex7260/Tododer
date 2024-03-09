package kanti.tododer.ui.common

import kanti.tododer.ui.components.todo.TodoData

data class TodoDataWithGroup(
	val todoData: TodoData = TodoData(),
	val group: String? = null
)