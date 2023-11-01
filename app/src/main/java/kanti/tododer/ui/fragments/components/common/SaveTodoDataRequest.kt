package kanti.tododer.ui.fragments.components.common

import kanti.tododer.data.model.common.Todo

data class SaveTodoDataRequest<Data>(
	val todo: Todo,
	val data: Data
)