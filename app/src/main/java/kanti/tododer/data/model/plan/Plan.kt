package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.PlanEntity

data class Plan(
	val id: Int = 0,
	val parentId: String = "",
	val title: String = "",
	val remark: String = ""
)

val Plan.fullId: String
	get() {
		return "plan-$id"
	}

val Plan.asNewPlanEntity: PlanEntity
	get() {
		return PlanEntity(
			parentId = parentId,
			title = title,
			remark = remark
		)
	}

val Plan.asPlanEntity: PlanEntity
	get() {
		return PlanEntity(
			id = id,
			parentId = parentId,
			title = title,
			remark = remark
		)
	}
