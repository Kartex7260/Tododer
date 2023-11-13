package kanti.tododer.domain.gettodowithprogeny.plan

import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.task.BaseTask

data class PlanWithProgeny(
	val plans: List<BasePlan>,
	val tasks: List<BaseTask>
)
