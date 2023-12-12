package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.task.Task

object ItemListTodoViewHolderFactory: TodoViewHolder.Factory {
	override fun createTaskViewHolder(
		todo: kanti.tododer.data.task.Task,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = TaskViewHolder(todo, layoutInflater, root, attachToRoot)

	override fun createPlanViewHolder(
		todo: kanti.tododer.data.model.plan.Plan,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = PlanViewHolder(todo, layoutInflater, root, attachToRoot)

}