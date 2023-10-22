package kanti.tododer.ui.fragments.components.todo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId

class TodoViewHolderManager(
	private val layoutInflater: LayoutInflater,
	private val root: ViewGroup? = null,
) {

	private val todoViewHolderHashMap: HashMap<String, TodoViewHolder> = HashMap()

	fun remove(todoElement: Todo) {
		todoViewHolderHashMap.remove(todoElement.fullId)
	}

	fun getViewHolder(todoElement: Todo, attachToRoot: Boolean = false): TodoViewHolder {
		return getViewHolder(todoElement) ?: createViewHolder(todoElement, attachToRoot)
	}

	private fun getViewHolder(todoElement: Todo): TodoViewHolder? {
		return todoViewHolderHashMap[todoElement.fullId]?.also { viewHolder ->
			if (viewHolder.todo != todoElement)
				viewHolder.todo = todoElement
		}
	}

	private fun createViewHolder(todoElement: Todo, attachToRoot: Boolean): TodoViewHolder {
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