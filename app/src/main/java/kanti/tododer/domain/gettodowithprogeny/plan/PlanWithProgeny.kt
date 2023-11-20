package kanti.tododer.domain.gettodowithprogeny.plan

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

data class PlanWithProgeny(
	val plans: List<Plan>,
	val tasks: List<Task>
)
