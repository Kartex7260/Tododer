package kanti.tododer.data.model.todo

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType

interface Todo {

	val id: Int
	val parentId: FullId
	val title: String
	val remark: String
	val done: Boolean
	val state: TodoState
}

private data class TodoImpl(
	override val id: Int,
	override val parentId: FullId,
	override val title: String,
	override val remark: String,
	override val done: Boolean,
	override val state: TodoState
) : Todo

fun Todo(
	id: Int = 0,
	parentId: FullId,
	title: String = "",
	remark: String = "",
	done: Boolean = false,
	state: TodoState = TodoState.Normal
): Todo {
	return TodoImpl(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done,
		state = state
	)
}

fun Todo.toTodo(
	id: Int = this.id,
	parentId: FullId = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done,
	state: TodoState = this.state
): Todo {
	if (
		this is TodoImpl &&
		id == this.id &&
		parentId == this.parentId &&
		title == this.title &&
		remark == this.remark &&
		done == this.done &&
		state == this.state
	)
		return this
	return Todo(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done,
		state = state
	)
}

fun Todo.toFullId(): FullId {
	return FullId(id, FullIdType.Todo)
}