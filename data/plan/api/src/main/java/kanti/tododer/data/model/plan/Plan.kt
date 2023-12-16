package kanti.tododer.data.model.plan

interface Plan {

	val id: Int
	val title: String
	val archived: Boolean
	val type: PlanType

}

enum class PlanType {
	All,
	Default,
	Custom;

	companion object {
		val DefaultValue = Custom
	}
}

private data class PlanImpl(
	override val id: Int,
	override val title: String,
	override val archived: Boolean,
	override val type: PlanType
) : Plan

fun Plan(
	id: Int = 0,
	title: String = "",
	archived: Boolean = false,
	type: PlanType = PlanType.DefaultValue,
	apply: (Plan.() -> Unit)? = null
): Plan {
	return PlanImpl(
		id = id,
		title = title,
		archived = archived,
		type = type
	).also { plan ->
		apply?.invoke(plan)
	}
}

fun Plan.toPlan(
	id: Int = this.id,
	title: String = this.title,
	archived: Boolean = this.archived,
	type: PlanType = this.type
): Plan {
	if (
		this is PlanImpl &&
		id == this.id &&
		title == this.title &&
		archived == this.archived &&
		type == this.type
	) {
		return this
	}
	return Plan(
		id = id,
		title = title,
		archived = archived,
		type = type
	)
}