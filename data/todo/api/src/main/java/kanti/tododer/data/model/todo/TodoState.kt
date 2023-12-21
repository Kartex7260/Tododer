package kanti.tododer.data.model.todo

sealed class TodoState {

	data object Normal : TodoState()
}