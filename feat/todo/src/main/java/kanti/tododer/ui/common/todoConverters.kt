package kanti.tododer.ui.common

import kanti.tododer.data.model.todo.Todo
import kanti.tododer.ui.components.todo.TodoData

fun Todo.toUiState(
	selected: Boolean = false,
	visible: Boolean = true
): TodoUiState = TodoUiState(
	selected = selected,
	visible = visible,
	data = toData()
)

fun Todo.toData(): TodoData = TodoData(
	id = id,
	title = title,
	remark = remark,
	isDone = done
)