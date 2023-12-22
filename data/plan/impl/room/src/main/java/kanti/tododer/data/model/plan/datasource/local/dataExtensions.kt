package kanti.tododer.data.model.plan.datasource.local

import kanti.sl.StateLanguage
import kanti.sl.deserialize
import kanti.sl.serialize
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.room.plan.PlanEntity

fun Plan.toPlanEntity(sl: StateLanguage): PlanEntity {
	return PlanEntity(
		id = id,
		title = title,
		state = sl.serialize(state),
		type = type.toString()
	)
}

fun PlanEntity.toPlan(sl: StateLanguage): Plan {
	return Plan(
		id = id,
		title = title,
		state = sl.deserialize(state),
		type = try {
			PlanType.valueOf(type)
		} catch (_: IllegalArgumentException) {
			PlanType.DefaultValue
		}
	)
}