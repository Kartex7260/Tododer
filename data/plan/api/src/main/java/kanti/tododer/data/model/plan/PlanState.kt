package kanti.tododer.data.model.plan

sealed class PlanState(
	val name: String
) {

	data object Normal : PlanState("PlanState.Normal")
}