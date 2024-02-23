package kanti.tododer.ui.common

import kanti.tododer.ui.components.todo.TodoData

data class TodoUiState(
    val selected: Boolean = false,
    val visible: Boolean = false,
    val data: TodoData = TodoData()
)
