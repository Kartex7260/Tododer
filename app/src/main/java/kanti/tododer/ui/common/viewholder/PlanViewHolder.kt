package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import kanti.fillingprogressview.FillingProgressView
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.model.plan.asPlan

class PlanViewHolder(
	todo: Todo,
	layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, R.layout.view_plan_list_item, root, attachToRoot) {

	override val type: Todo.Type = Todo.Type.PLAN

	override fun onBindData(view: View, todo: Todo) {
		val plan = todo.asPlan
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
		view.findViewById<FillingProgressView>(R.id.fillingProgressViewPlanListItem).apply {
			event(EVENT_PROGRESS_REQUEST, plan) { todoProgress ->
				if (todoProgress == null || todoProgress !is GetRepositoryResult<*>) {
					return@event
				}
				val sucTodoProgress = todoProgress.asSuccess ?: return@event
				progress = sucTodoProgress.value as Float
			}
		}
		view.findViewById<ImageButton>(R.id.imageButtonPlanMore).apply {
			setOnClickListener { v ->
				view.showContextMenu(v.x, v.y)
			}
		}
	}

	companion object {

		const val EVENT_PROGRESS_REQUEST = 0

	}

}