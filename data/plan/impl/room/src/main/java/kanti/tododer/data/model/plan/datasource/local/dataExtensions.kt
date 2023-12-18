package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.room.plan.PlanEntity

fun Plan.toPlanEntity(): PlanEntity {
	return PlanEntity(
		id = id,
		title = title,
		archived = archived,
		type = type.toString()
	)
}

fun PlanEntity.toPlan(): Plan {
	return Plan(
		id = id,
		title = title,
		archived = archived,
		type = try {
			PlanType.valueOf(type)
		} catch (_: IllegalArgumentException) {
			PlanType.DefaultValue
		}
	)
}