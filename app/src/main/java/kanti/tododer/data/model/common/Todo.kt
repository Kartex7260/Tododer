package kanti.tododer.data.model.common

interface Todo : IdOwner, ParentOwner {

	val type: Type

	val fullId: String get() {
		return FullIds.from(this)
	}

	enum class Type {
		PLAN,
		TASK
	}

	fun checkType(type: Type) {
		if (this.type != type)
			throw IllegalStateException("This todo (${this.type}) is not $type")
	}

	companion object {

		val Empty: Todo = object : Todo {
			override val type: Type = Type.TASK
			override val id: Int = 0
			override val parentId: String = ""
		}

	}

}