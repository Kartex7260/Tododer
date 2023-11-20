package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.Task

open class TaskViewHolder(
	todo: Todo,
	layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, R.layout.view_task_list_item, root, attachToRoot) {

	override val type: Todo.Type = Todo.Type.TASK

	override fun onBindData(view: View, todo: Todo) {
		val task = todo as Task

		view.findViewById<TextView>(R.id.textViewTodoItemTaskTitle).apply {
			text = task.title
		}
		view.findViewById<CheckBox>(R.id.checkBoxTodoItemTaskDone).apply {
			isChecked = task.done
			setOnCheckedChangeListener { _, isChecked ->
				event(EVENT_IS_DONE, task, isChecked)
			}
		}
		view.findViewById<ImageButton>(R.id.imageButtonTaskMore).apply {
			setOnClickListener { v ->
				view.showContextMenu(v.x, v.y)
			}
		}
	}

	companion object {

		const val EVENT_IS_DONE = 0

	}

}