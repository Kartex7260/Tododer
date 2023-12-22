package kanti.tododer.data.model.plan

interface Plan {

	val id: Int
	val title: String
	val state: PlanState
	val type: PlanType

}

private data class PlanImpl(
	override val id: Int,
	override val title: String,
	override val state: PlanState,
	override val type: PlanType
) : Plan

fun Plan(
	id: Int = 0,
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
	id: Int = this.id,
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