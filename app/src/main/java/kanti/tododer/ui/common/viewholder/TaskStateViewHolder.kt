package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.google.android.material.checkbox.MaterialCheckBox
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.task.asTask

class TaskStateViewHolder(
	todo: kanti.tododer.data.model.common.Todo,
	private val layoutInflater: LayoutInflater,
	root: ViewGroup? = RootDefault,
	attachToRoot: Boolean = AttachToRootDefault
) : TodoViewHolder(todo, layoutInflater, NonResource, root, attachToRoot),
	TaskStateViewOwner {

	override val type: kanti.tododer.data.model.common.Todo.Type = kanti.tododer.data.model.common.Todo.Type.TASK

	override val stateView: MaterialCheckBox
		get() = view as MaterialCheckBox

	override fun createView(): View {
		return MaterialCheckBox(layoutInflater.context).apply {
			setOnCheckedChangeListener { _, isChecked ->
				event(EVENT_IS_DONE, todo, isChecked)
			}
		}
	}

	override fun onBindData(view: View, todo: kanti.tododer.data.model.common.Todo) {
		val task = todo.asTask

		val checkBox = view as CheckBox
		checkBox.isChecked = task.done
	}

	companion object {

		const val EVENT_IS_DONE = TaskViewHolder.EVENT_IS_DONE

	}

}

interface TaskStateViewOwner : TodoStateViewOwner<MaterialCheckBox>
