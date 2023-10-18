package kanti.tododer.ui.fragments.components.todo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.ui.state.TodoElement
import kanti.tododer.ui.state.fullId

class TodoViewHolderManager(
	private val layoutInflater: LayoutInflater,
	private val root: ViewGroup? = null,
) {

	private val todoViewHolderHashMap: HashMap<String, TodoViewHolder> = HashMap()

	fun getViewHolder(todoElement: TodoElement, attachToRoot: Boolean = false): TodoViewHolder {
		return getViewHolder(todoElement) ?: createViewHolder(todoElement, attachToRoot)
	}

	private fun getViewHolder(todoElement: TodoElement): TodoViewHolder? {
		return todoViewHolderHashMap[todoElement.fullId]?.also { viewHolder ->
			if (viewHolder.todoElement != todoElement)
				viewHolder.todoElement = todoElement
		}
	}

	private fun createViewHolder(todoElement: TodoElement, attachToRoot: Boolean): TodoViewHolder {
		return TodoViewHolder.newInstance(
			todoElement,
			layoutInflater,
			root,
			attachToRoot
		).also { viewHolder ->
			todoViewHolderHashMap[todoElement.fullId] = viewHolder
		}
	}

}