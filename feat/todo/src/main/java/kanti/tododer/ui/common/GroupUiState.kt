package kanti.tododer.ui.common

import androidx.compose.runtime.Immutable

@Immutable
data class GroupUiState(
	val name: String? = null,
	val expand: Boolean = true,
	val todos: List<TodoUiState> = listOf()
) {

	val selected: Boolean get() = todos.fold(true) { acc, todoUiState ->
		acc and todoUiState.selected
	}
}