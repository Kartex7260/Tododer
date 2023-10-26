package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId

class TodoViewHolderManager(
	private val todoViewHolderFactory: TodoViewHolder.Factory,
	private var layoutInflater: LayoutInflater,
	var root: ViewGroup? = null,
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
			todoViewHolderFactory,
			todoElement,
			layoutInflater,
			root,
			attachToRoot
		).also { viewHolder ->
			todoViewHolderHashMap[todoElement.fullId] = viewHolder
		}
	}

}