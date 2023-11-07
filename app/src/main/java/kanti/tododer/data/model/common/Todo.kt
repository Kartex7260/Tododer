package kanti.tododer.data.model.common

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

interface Todo : IdOwner {

	val type: Type

	enum class Type {
		PLAN,
		TASK
	}

	companion object {

		val Empty: Todo = object : Todo {
			override val type: Type = Type.TASK
			override val id: Int = 0
		}

	}

}