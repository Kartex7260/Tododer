package kanti.tododer.data.model.plan.archive

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan

data class ArchivePlan(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val hollow: Boolean = BaseArchivePlan.HOLLOW_DEFAULT
) : BaseArchivePlan {

	override val type: Todo.Type = Todo.Type.PLAN

}

val Todo.asArchivePlan: ArchivePlan get() {
	checkType(Todo.Type.PLAN)
	if (this !is BaseArchivePlan)
		throw IllegalStateException("This todo ($this) is not implementation BaseArchivePlan")
	return toArchivePlan()
}

fun BasePlan.toArchivePlan(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	hollow: Boolean? = null
): ArchivePlan {
	if (this is ArchivePlan && id == this.id && parentId == this.parentId && title == this.title &&
		remark == this.remark) {
		if (hollow == null || hollow == this.hollow)
			return this
	}
	return ArchivePlan(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		hollow = hollow ?: if (this is BaseArchivePlan)
			this.hollow
		else
			BaseArchivePlan.HOLLOW_DEFAULT
	)
}