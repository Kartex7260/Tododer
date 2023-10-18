package kanti.tododer.ui.fragments.components.todo_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kanti.tododer.R
import kanti.tododer.ui.state.TodoElement

class PlanViewHolder(
	todo: TodoElement,
	private val layoutInflater: LayoutInflater,
	private val root: ViewGroup? = null,
	private val attachToRoot: Boolean = false
) : TodoViewHolder(todo, layoutInflater, R.layout.view_plan_list_item, root, attachToRoot) {

	override val type: TodoElement.Type = TodoElement.Type.PLAN

	override fun bindData(view: View, todo: TodoElement) {
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