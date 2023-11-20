package kanti.tododer.data.model.common

data class FullId(
	override val type: Todo.Type,
	override val id: Int
) : Todo

val Todo.toFullId: FullId get() {
	return FullId(
		type = type,
		id = id
	)
}