package kanti.tododer.ui.common

data class TodosUiState(
    val selection: Boolean = false,
    val todos: List<TodoUiState> = listOf()
)
