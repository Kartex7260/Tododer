package kanti.tododer.ui.common

import kanti.tododer.ui.components.todo.TodoData
import javax.annotation.concurrent.Immutable

@Immutable
data class TodoUiState(
    override val selected: Boolean = false,
    override val visible: Boolean = true,
    val data: TodoData = TodoData()
) : Visible, Selectable
