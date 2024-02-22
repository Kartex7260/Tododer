package kanti.tododer.data.model.plan

enum class PlanType(val isEditable: Boolean) {
	All(false),
	Default(false),
	Custom(true);

	companion object {
		val DefaultValue = Custom
	}
}