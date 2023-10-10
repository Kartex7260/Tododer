package kanti.tododer.ui.state

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

val Task.toTodoElement: TodoElement
	get() = TodoElement(this)

val Plan.toTodoElement: TodoElement
	get() = TodoElement(this)

class TodoElement {

	var value: Any
		private set

	var type: Type
		private set

	val toTask: Task
		get() = value as Task

	val toPlan: Plan
		get() = value as Plan

	constructor(task: Task) {
		value = task
		type = Type.TASK
	}

	constructor(plan: Plan) {
		value = plan
		type = Type.PLAN
	}

	enum class Type {
		PLAN,
		TASK
	}

}