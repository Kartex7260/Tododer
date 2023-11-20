package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.Todo

data class PlanImpl(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = ""
) : Plan {
	override val type: Todo.Type = Todo.Type.PLAN

	companion object {

		val Empty = PlanImpl()

	}

}

fun Plan.toPlan(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark
): Plan {
	if (this is PlanImpl && id == this.id && parentId == this.parentId &&
		title == this.title && remark == this.remark)
		return this
	return PlanImpl(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark
	)
}

val Todo.asPlan: Plan get() {
	checkType(Todo.Type.PLAN)
	if (this !is Plan)
		throw IllegalStateException("This todo is not implementation BasePlan")
	return toPlan()
}

