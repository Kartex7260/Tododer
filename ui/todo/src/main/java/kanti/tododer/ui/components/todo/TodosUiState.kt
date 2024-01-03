package kanti.tododer.ui.components.todo

import androidx.compose.runtime.Stable

@Stable
data class TodosUiState(
	val todos: List<TodoUiState> = listOf()
)
