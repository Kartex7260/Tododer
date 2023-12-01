package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.RemarkOwner
import kanti.tododer.data.model.common.TitleOwner
import kanti.tododer.data.model.common.Todo

interface Plan : Todo, TitleOwner, RemarkOwner

fun Plan(
	id: Int = 0,
	parentId: String = "",
	title: String = "",
	remark: String = "",
	apply: (Plan.() -> Unit)? = null
): Plan {
	return PlanImpl(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark
	).also { plan ->
		apply?.invoke(plan)
	}
}