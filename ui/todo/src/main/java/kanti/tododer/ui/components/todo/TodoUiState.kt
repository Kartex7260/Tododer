package kanti.tododer.ui.components.todo

import androidx.compose.runtime.Stable

@Stable
data class TodoUiState(
	val id: Int,
	val title: String,
	val isDone: Boolean
)
