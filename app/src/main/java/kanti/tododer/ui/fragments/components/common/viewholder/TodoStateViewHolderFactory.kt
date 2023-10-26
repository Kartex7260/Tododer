package kanti.tododer.ui.fragments.components.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.common.Todo

object TodoStateViewHolderFactory : TodoViewHolder.Factory {

	override fun createTaskViewHolder(
		todo: Todo,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = TaskStateViewHolder(todo, layoutInflater, root, attachToRoot)

	override fun createPlanViewHolder(
		todo: Todo,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder = TodoViewHolder.empty(todo, layoutInflater)
}