package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.Todo

data class Plan(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = ""
) : BasePlan {
	override val type: Todo.Type = Todo.Type.PLAN

	companion object {

		val Empty = Plan()

	}

}

fun BasePlan.toPlan(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark
): Plan {
	if (this is Plan && id == this.id && parentId == this.parentId &&
		title == this.title && remark == this.remark)
		return this
	return Plan(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark
	)
}

val Todo.asPlan: Plan get() {
	checkType(Todo.Type.PLAN)
	if (this !is BasePlan)
		throw IllegalStateException("This todo is not implementation BasePlan")
	return toPlan()
}

