package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.google.android.material.checkbox.MaterialCheckBox
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.asTask

class TaskStateViewHolder(
	todo: Todo,
	private val layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, NonResource, root, attachToRoot),
	TaskStateViewOwner {

	override val type: Todo.Type = Todo.Type.TASK

	override val stateView: MaterialCheckBox
		get() = view as MaterialCheckBox

	override fun createView(): View {
		return MaterialCheckBox(layoutInflater.context).apply {
			setOnCheckedChangeListener { _, isChecked ->
				event(EVENT_IS_DONE, todo, isChecked)
			}
		}
	}

	override fun onBindData(view: View, todo: Todo) {
		val task = todo.asTask

		val checkBox = view as CheckBox
		checkBox.isChecked = task.done
	}

	companion object {

		const val EVENT_IS_DONE = TaskViewHolder.EVENT_IS_DONE

	}

}

interface TaskStateViewOwner : TodoStateViewOwner<MaterialCheckBox>
