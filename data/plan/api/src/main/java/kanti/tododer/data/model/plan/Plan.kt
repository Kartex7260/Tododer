package kanti.tododer.data.model.plan

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType

interface Plan {

	val id: Long
	val title: String
	val state: PlanState
	val type: PlanType

}

private data class PlanImpl(
	override val id: Long,
	override val title: String,
	override val state: PlanState,
	override val type: PlanType
) : Plan

fun Plan(
	id: Long = 0,
	title: String = "",
	state: PlanState = PlanState.Normal,
	type: PlanType = PlanType.DefaultValue,
	apply: (Plan.() -> Unit)? = null
): Plan {
	return PlanImpl(
		id = id,
		title = title,
		state = state,
		type = type
	).also { plan ->
		apply?.invoke(plan)
	}
}

fun Plan.toPlan(
	id: Long = this.id,
	title: String = this.title,
	state: PlanState = this.state,
	type: PlanType = this.type
): Plan {
	if (
		this is PlanImpl &&
		id == this.id &&
		title == this.title &&
		state == this.state &&
		type == this.type
	) {
		return this
	}
	return Plan(
		id = id,
		title = title,
		state = state,
		type = type
	)
}

fun Plan.toFullId(): FullId {
	return FullId(id, FullIdType.Plan)
}