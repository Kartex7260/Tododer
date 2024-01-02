package kanti.tododer.ui.components.todo

import androidx.compose.runtime.Stable

@Stable
data class TodoListUiState(
	val todos: List<TodoUiState> = listOf()
)
