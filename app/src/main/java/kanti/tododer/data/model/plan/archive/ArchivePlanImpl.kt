package kanti.tododer.data.model.plan.archive

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan

data class ArchivePlanImpl(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val hollow: Boolean = ArchivePlan.HOLLOW_DEFAULT
) : ArchivePlan {

	override val type: Todo.Type = Todo.Type.PLAN

}

val Todo.asArchivePlan: ArchivePlan get() {
	checkType(Todo.Type.PLAN)
	if (this !is ArchivePlan)
		throw IllegalStateException("This todo ($this) is not implementation BaseArchivePlan")
	return toArchivePlan()
}

fun Plan.toArchivePlan(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	hollow: Boolean? = null
): ArchivePlan {
	if (this is ArchivePlanImpl && id == this.id && parentId == this.parentId && title == this.title &&
		remark == this.remark) {
		if (hollow == null || hollow == this.hollow)
			return this
	}
	return ArchivePlanImpl(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		hollow = hollow ?: if (this is ArchivePlan)
			this.hollow
		else
			ArchivePlan.HOLLOW_DEFAULT
	)
}