package kanti.tododer.data.model.common

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

abstract class Todo : IdOwner {

	abstract val type: Type

	override fun toString(): String {
		return "Todo(id=$id type=$type)"
	}

	enum class Type {
		PLAN,
		TASK
	}

}