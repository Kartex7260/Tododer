package kanti.tododer.ui.fragments.components.todo_list

import kanti.tododer.data.model.common.Todo

fun interface TodoEventListener {

	fun onEvent(type: Int, todo: Todo, value: Any?)

}