package kanti.tododer.ui.common

import kanti.tododer.ui.components.todo.TodoData
import javax.annotation.concurrent.Immutable

@Immutable
data class TodoUiState(
    val selected: Boolean = false,
    val visible: Boolean = false,
    val data: TodoData = TodoData()
)
