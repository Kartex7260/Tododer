package kanti.tododer.data.model.common

data class FullId(
	override val type: Todo.Type,
	override val id: Int
) : Todo {
	override val parentId: String = "noParent"
}

val Todo.toFullId: FullId get() {
	return FullId(
		type = type,
		id = id
	)
}