package kanti.tododer.ui.fragments.screens.todo_archive_list.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.common.Todo
import kanti.tododer.ui.common.viewholder.TodoViewHolder

object ArchiveTodoViewHolderFactory : TodoViewHolder.Factory {

	override fun createTaskViewHolder(
		todo: Todo,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder {
		return ArchiveTaskViewHolder(todo, layoutInflater, root, attachToRoot)
	}

	override fun createPlanViewHolder(
		todo: Todo,
		layoutInflater: LayoutInflater,
		root: ViewGroup?,
		attachToRoot: Boolean
	): TodoViewHolder {
		return ArchivePlanViewHolder(todo, layoutInflater, root, attachToRoot)
	}

}