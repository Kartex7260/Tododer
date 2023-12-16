package kanti.tododer.data.model.todo.datasource.local

import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.room.todo.TodoEntity

fun Todo.toTodoEntity(): TodoEntity {
	return TodoEntity(
		id = id,
		parentId = parentId.toString(),
		title = title,
		remark = remark,
		done = done
	)
}

fun TodoEntity.toTodo(): Todo {
	return Todo(
		id = id,
		parentId = ParentId.from(parentId),
		title = title,
		remark = remark,
		done = done
	)
}