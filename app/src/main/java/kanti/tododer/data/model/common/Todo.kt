package kanti.tododer.data.model.common

import kanti.tododer.data.model.common.fullid.FullIds

interface Todo : IdOwner, ParentOwner {

	val type: Type

	val fullId: String get() {
		return FullIds.from(this)
	}

	enum class Type {
		PLAN,
		TASK
	}

	companion object {

		val Empty: Todo = object : Todo {
			override val type: Todo.Type = Todo.Type.TASK
			override val id: Int = 0
			override val parentId: String = ""
		}

	}

}
