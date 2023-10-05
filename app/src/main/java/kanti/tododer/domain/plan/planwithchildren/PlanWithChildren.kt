package kanti.tododer.domain.plan.planwithchildren

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

data class PlanWithChildren(
	val plan: Plan? = null,
	val childPlans: List<Plan> = listOf(),
	val childTasks: List<Task> = listOf()
)
