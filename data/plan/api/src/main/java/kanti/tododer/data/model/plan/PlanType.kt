package kanti.tododer.data.model.plan

enum class PlanType {
	All,
	Default,
	Custom;

	companion object {
		val DefaultValue = Custom
	}
}