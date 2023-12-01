package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

object ItemListTodoViewHolderFactory: TodoViewHolder.Factory {
	override fun createTaskViewHolder(
		todo: Task,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = TaskViewHolder(todo, layoutInflater, root, attachToRoot)

	override fun createPlanViewHolder(
		todo: Plan,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = PlanViewHolder(todo, layoutInflater, root, attachToRoot)

}