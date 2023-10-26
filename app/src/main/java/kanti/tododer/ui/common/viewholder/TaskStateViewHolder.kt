package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.toTask

class TaskStateViewHolder(
	todo: Todo,
	private val layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, NonResource, root, attachToRoot) {

	override val type: Todo.Type = Todo.Type.TASK

	override fun createView(): View {
		return CheckBox(layoutInflater.context).apply {
			setOnCheckedChangeListener { _, isChecked ->
				event(EVENT_IS_DONE, todo, isChecked)
			}
		}
	}

	override fun bindData(view: View, todo: Todo) {
		val task = todo.toTask

		val checkBox = view as CheckBox
		checkBox.isChecked = task.done
	}

	companion object {

		const val EVENT_IS_DONE = TaskViewHolder.EVENT_IS_DONE

	}

}