package kanti.tododer.data.model.todo

sealed class TodoState(
	val name: String
) {

	data object Normal : TodoState("TodoState.Normal")
}