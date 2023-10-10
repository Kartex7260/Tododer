package kanti.tododer.ui.screens.screen.todo_detail.viewmodel.uistate

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.domain.plan.planwithchildren.PlanWithChildren
import kanti.tododer.domain.task.taskwithchildren.TaskWithChildren

val TaskWithChildren.toTodoElement: TodoElement
	get() = TodoElement(this)

val PlanWithChildren.toTodoElement: TodoElement
	get() = TodoElement(this)

class TodoElement {

	var value: Any
		private set

	var type: Type
		private set

	val toTask: TaskWithChildren
		get() = value as TaskWithChildren

	val toPlan: PlanWithChildren
		get() = value as PlanWithChildren

	constructor(task: TaskWithChildren) {
		value = task
		type = Type.TASK
	}

	constructor(plan: PlanWithChildren) {
		value = plan
		type = Type.PLAN
	}

	enum class Type {
		PLAN,
		TASK
	}

}