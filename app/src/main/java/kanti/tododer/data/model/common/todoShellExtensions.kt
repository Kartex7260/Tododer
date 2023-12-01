package kanti.tododer.data.model.common

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

val Todo.fullId: String
	get() = FullIds.from(this)

val Todo.toPlan: Plan get() {
	check(Todo.Type.PLAN)
	return this as Plan
}

val Todo.toTask: Task get() {
	check(Todo.Type.TASK)
	return this as Task
}

val Todo.toFullId: FullId get() {
	return FullId(type, id)
}

private fun Todo.check(typeExpected: Todo.Type) {
	check(typeExpected == type) {
		"Try get $typeExpected, but todo is $type"
	}
}