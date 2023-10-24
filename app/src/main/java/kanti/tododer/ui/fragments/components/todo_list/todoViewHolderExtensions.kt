package kanti.tododer.ui.fragments.components.todo_list

import android.util.Log
import kanti.tododer.common.Const

fun TodoViewHolder.setEventListenerIfNull(eventListener: TodoEventListener) {
	fun log(mes: String) = Log.d(
		javaClass.simpleName,
		"setEventListenerIfNull(TodoEventListener = TodoEventListener${eventListener.hashCode()}): $mes"
	)

	if (this.eventListener == null) {
		log("eventListener is null. Set new event listener")
		setEventListener(eventListener)
	} else {
		log("eventListener isn't null.")
	}
}