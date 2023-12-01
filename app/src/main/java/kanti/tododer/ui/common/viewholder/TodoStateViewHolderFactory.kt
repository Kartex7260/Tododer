package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task

object TodoStateViewHolderFactory : TodoViewHolder.Factory {

	override fun createTaskViewHolder(
		todo: Task,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = TaskStateViewHolder(todo, layoutInflater, root, attachToRoot)

	override fun createPlanViewHolder(
		todo: Plan,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = PlanStateViewHolder(todo, layoutInflater, root, attachToRoot)
}