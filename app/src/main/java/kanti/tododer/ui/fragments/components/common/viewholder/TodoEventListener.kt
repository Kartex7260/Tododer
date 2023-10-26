package kanti.tododer.ui.fragments.components.common.viewholder

import kanti.tododer.data.model.common.Todo

fun interface TodoEventListener {

	fun onEvent(type: Int, todo: Todo, value: Any?)

}