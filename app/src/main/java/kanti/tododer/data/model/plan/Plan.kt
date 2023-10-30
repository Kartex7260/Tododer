package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.datasource.local.PlanEntity

data class Plan(
	override val id: Int = 0,
	val parentId: String = "",
	val title: String = "",
	val remark: String = ""
) : Todo() {
	override val type: Todo.Type = Todo.Type.PLAN

	companion object {

		val Empty = Plan()

	}

}

val Plan.asPlanEntity: PlanEntity
	get() {
		return PlanEntity(
			id = id,
			parentId = parentId,
			title = title,
			remark = remark
		)
	}
