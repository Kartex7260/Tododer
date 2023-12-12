package kanti.tododer.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import kanti.tododer.data.model.common.Todo

class TodoViewHolderManager(
	private val todoViewHolderFactory: TodoViewHolder.Factory,
	private var layoutInflater: LayoutInflater,
	var root: ViewGroup? = null,
) {

	private val todoViewHolderHashMap: HashMap<String, TodoViewHolder> = HashMap()

	var current: TodoViewHolder? = null
		private set

	fun remove(todoElement: kanti.tododer.data.model.common.Todo) {
		todoViewHolderHashMap.remove(todoElement.fullId)
	}

	fun getViewHolder(todoElement: kanti.tododer.data.model.common.Todo, attachToRoot: Boolean = false, setCurrent: Boolean = false): TodoViewHolder {
		return (getViewHolder(todoElement) ?: createViewHolder(todoElement, attachToRoot)).also {
			if (setCurrent)
				current = it
		}
	}

	private fun getViewHolder(todoElement: kanti.tododer.data.model.common.Todo): TodoViewHolder? {
		return todoViewHolderHashMap[todoElement.fullId]?.also { viewHolder ->
			viewHolder.todo = todoElement
		}
	}

	private fun createViewHolder(todoElement: kanti.tododer.data.model.common.Todo, attachToRoot: Boolean): TodoViewHolder {
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