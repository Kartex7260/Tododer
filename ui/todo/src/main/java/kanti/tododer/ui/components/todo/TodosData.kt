package kanti.tododer.ui.components.todo

import androidx.compose.runtime.Stable

@Stable
data class TodosData(
	val todos: List<TodoData> = listOf()
)
