package kanti.tododer.ui.fragments.screens.todo_archive_list.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kanti.fillingprogressview.FillingProgressView
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.archive.ArchivePlan
import kanti.tododer.ui.common.viewholder.PlanViewHolder

class ArchivePlanViewHolder(
	todo: Todo,
	layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : PlanViewHolder(todo, layoutInflater, root, attachToRoot) {

	init {
		if (todo !is ArchivePlan)
			throw IllegalArgumentException("ArchivePlanViewHolder work only with BaseArchivePlan")
	}

	override fun onBindData(view: View, todo: Todo) {
		super.onBindData(view, todo)
		val archivePlan = todo as ArchivePlan

		view.findViewById<FillingProgressView>(R.id.fillingProgressViewPlanListItem).apply {
			isEnabled = !archivePlan.hollow
		}
	}

}