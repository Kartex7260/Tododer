package kanti.tododer.ui.fragments.screens.todo_archive_list.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.archive.BaseArchiveTask
import kanti.tododer.data.model.task.archive.asArchiveTask
import kanti.tododer.ui.common.viewholder.TaskViewHolder

class ArchiveTaskViewHolder(
	todo: Todo,
	layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TaskViewHolder(todo, layoutInflater, root, attachToRoot) {

	init {
		if (todo !is BaseArchiveTask)
			throw IllegalArgumentException("ArchiveTaskViewHolder work only with BaseArchiveTask")
	}

	override fun onBindData(view: View, todo: Todo) {
		super.onBindData(view, todo)
		val archiveTask = todo.asArchiveTask

		view.findViewById<CheckBox>(R.id.checkBoxTodoItemTaskDone).apply {
			isEnabled = !archiveTask.hollow
		}
	}

}