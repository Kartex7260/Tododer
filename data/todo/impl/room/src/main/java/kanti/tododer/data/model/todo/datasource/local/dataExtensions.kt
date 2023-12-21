package kanti.tododer.data.model.todo.datasource.local

import kanti.sl.StateLanguage
import kanti.sl.deserialize
import kanti.sl.serialize
import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.room.todo.TodoEntity

fun Todo.toTodoEntity(sl: StateLanguage): TodoEntity {
	return TodoEntity(
		id = id,
		parentId = parentId.toString(),
		title = title,
		remark = remark,
		done = done,
		state = sl.serialize(state)
	)
}

fun TodoEntity.toTodo(sl: StateLanguage): Todo {
	return Todo(
		id = id,
		parentId = ParentId.from(parentId),
		title = title,
		remark = remark,
		done = done,
		state = sl.deserialize(state)
	)
}