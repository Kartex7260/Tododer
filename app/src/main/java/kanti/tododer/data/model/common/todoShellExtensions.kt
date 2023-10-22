package kanti.tododer.data.model.common

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

val Todo.fullId: String
	get() = FullIds.from(this)

val Todo.toPlan: Plan get() {
	check("plan", type)
	return this as Plan
}

val Todo.toTask: Task get() {
	check("task", type)
	return this as Task
}

val Todo.toFullId: FullId get() {
	return FullId(type, id)
}

private fun check(typeString: String, type: Todo.Type) {
	check(type == Todo.Type.PLAN) {
		"Try get $typeString, but todo is $type"
	}
}