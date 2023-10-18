package kanti.tododer.ui.fragments.components.todo_list

fun TodoViewHolder.setEventListenerIfNull(eventListener: TodoEventListener) {
	if (this.eventListener == null)
		setEventListener(eventListener)
}