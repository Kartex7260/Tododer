package kanti.tododer.data.model.todo

import kanti.tododer.data.model.ParentId

interface Todo {

	val id: Int
	val parentId: ParentId
	val title: String
	val remark: String
	val done: Boolean
}

private data class TodoImpl(
	override val id: Int,
	override val parentId: ParentId,
	override val title: String,
	override val remark: String,
	override val done: Boolean
) : Todo

fun Todo(
	id: Int = 0,
	parentId: ParentId,
	title: String = "",
	remark: String = "",
	done: Boolean = false
): Todo {
	return TodoImpl(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}

fun Todo.toTodo(
	id: Int = this.id,
	parentId: ParentId = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done
): Todo {
	if (
		this is TodoImpl &&
		id == this.id &&
		parentId == this.parentId &&
		title == this.title &&
		remark == this.remark &&
		done == this.done
	)
		return this
	return Todo(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}