package kanti.tododer.data.model.common.fullid

import kanti.tododer.data.model.common.Todo

data class FullId(
	override val type: Todo.Type,
	override val id: Int
) : Todo {
	override val parentId: String = ""
}

val Todo.asFullId: FullId
	get() {
	if (this is FullId)
		return this
	return FullId(
		type = type,
		id = id
	)
}