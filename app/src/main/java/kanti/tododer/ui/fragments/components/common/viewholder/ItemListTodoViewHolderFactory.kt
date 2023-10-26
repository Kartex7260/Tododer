package kanti.tododer.ui.fragments.components.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.Task

object ItemListTodoViewHolderFactory: TodoViewHolder.Factory {
	override fun createTaskViewHolder(
		todo: Todo,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = TaskViewHolder(todo, layoutInflater, root, attachToRoot)

	override fun createPlanViewHolder(
		todo: Todo,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = PlanViewHolder(todo, layoutInflater, root, attachToRoot)

}