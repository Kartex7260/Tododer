package kanti.tododer.ui.screen.todo_detail.viewmodel

sealed class OnTodoDeletedUiState {

	data class ShowMessage(
		val title: String
	) : OnTodoDeletedUiState()

	data object HideMessage : OnTodoDeletedUiState()
}
