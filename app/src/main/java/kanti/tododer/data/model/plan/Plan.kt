package kanti.tododer.data.model.plan

import androidx.core.graphics.rotationMatrix
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.datasource.local.PlanEntity

data class Plan(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = ""
) : IPlan {
	override val type: Todo.Type = Todo.Type.PLAN

	companion object {

		val Empty = Plan()

	}

}

fun IPlan.toPlan(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark
): Plan {
	return Plan(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark
	)
}

