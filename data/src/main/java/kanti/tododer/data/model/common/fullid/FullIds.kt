package kanti.tododer.data.model.common.fullid

import kanti.tododer.data.model.common.Todo

object FullIds {

	private const val separator = '-'

	fun from(todo: Todo): String {
		val type = todo.type.toString()
		return from(type, todo.id)
	}

	private fun from(prefix: String, id: Int): String {
		return "$prefix$separator$id"
	}

	fun parseFullId(fullId: String): FullId? {
		val split = split(fullId)
		val type = getType(split)
		val id = getId(split)
		if (type == null || id == null)
			return null
		return FullId(type, id)
	}

	private fun getId(split: List<String>): Int? {
		if (split.size < 2)
			return null
		return split[1].toIntOrNull()
	}

	private fun getType(split: List<String>): Todo.Type? {
		if (split.isEmpty())
			return null
		val typeString = split[0]
		for (type in Todo.Type.entries) {
			if (typeString == type.toString())
				return type
		}
		return null
	}

	private fun split(fullId: String): List<String> {
		return fullId.split(separator)
	}

}