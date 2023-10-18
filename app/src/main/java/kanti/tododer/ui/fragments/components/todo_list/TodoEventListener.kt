package kanti.tododer.ui.fragments.components.todo_list

import kanti.tododer.ui.state.TodoElement

fun interface TodoEventListener {

	fun onEvent(type: Int, todo: TodoElement, value: Any?)

}