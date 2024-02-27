package kanti.tododer.ui.common

import androidx.compose.runtime.Immutable

@Immutable
data class TodosUiState(
    val selection: Boolean = false,
    val todos: List<TodoUiState> = listOf()
)
