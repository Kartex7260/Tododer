package kanti.tododer.ui.fragments.components.todo_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import kanti.tododer.R
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toTask

class TaskViewHolder(
	todo: Todo,
	layoutInflater: LayoutInflater,
	root: ViewGroup? = null,
	attachToRoot: Boolean = false
) : TodoViewHolder(todo, layoutInflater, R.layout.view_task_list_item, root, attachToRoot) {

	override val type: Todo.Type = Todo.Type.TASK

	override fun bindData(view: View, todo: Todo) {
		val task = todo.toTask

		view.findViewById<TextView>(R.id.textViewTodoItemTaskTitle).apply {
			text = task.title
		}
		view.findViewById<CheckBox>(R.id.checkBoxTodoItemTaskDone).apply {
			isChecked = task.done
			setOnCheckedChangeListener { _, isChecked ->
				onEvent(EVENT_IS_DONE, task, isChecked)
			}
		}
	}

	companion object {

		const val EVENT_IS_DONE = 0

	}

}