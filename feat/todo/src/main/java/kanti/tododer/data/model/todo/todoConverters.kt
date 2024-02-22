package kanti.tododer.data.model.todo

import kanti.tododer.ui.components.todo.TodoData

fun Todo.toTodoData(
	visible: Boolean = true
): TodoData {
	return TodoData(
		id = id,
		title = title,
		remark = remark,
		isDone = done,
		visible = visible
	)
}