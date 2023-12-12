package kanti.tododer.ui.common.viewholder

import kanti.tododer.data.model.common.Todo

fun interface TodoEventListener {

	fun onEvent(type: Int, todo: kanti.tododer.data.model.common.Todo, value: Any?, callback: TodoEventCallback?)

}

fun interface TodoEventCallback {

	fun callback(value: Any?)

}