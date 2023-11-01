package kanti.tododer.ui.fragments.components.todo_list.viewmodel

import android.view.ContextMenu
import kanti.tododer.data.model.common.Todo

data class TodoItemCreateContextMenuRequest(
	val todo: Todo,
	val contextMenu: ContextMenu
)
