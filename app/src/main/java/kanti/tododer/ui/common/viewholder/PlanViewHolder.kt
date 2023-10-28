package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toPlan

class PlanViewHolder(
	todo: Todo,
	layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, R.layout.view_plan_list_item, root, attachToRoot) {

	override val type: Todo.Type = Todo.Type.PLAN

	override fun onBindData(view: View, todo: Todo) {
		val plan = todo.toPlan
		view.findViewById<TextView>(R.id.textViewTodoItemPlanTitle).apply {
			text = plan.title
		}
		view.findViewById<TextView>(R.id.textViewTodoItemPlanRemark).apply {
			if (plan.remark.isBlank()) {
				visibility = View.GONE
				return@apply
			} else {
				visibility = View.VISIBLE
			}
			text = plan.remark
		}
	}

}