package kanti.tododer.data.model.common

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

interface Todo : IdOwner {

	val type: Type

	val fullId: String get() {
		return FullIds.from(this)
	}

	val toFullId: FullId get() {
		return FullId(
			type = type,
			id = id
		)
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
		}

	}

}