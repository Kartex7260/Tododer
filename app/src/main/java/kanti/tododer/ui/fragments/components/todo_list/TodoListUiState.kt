package kanti.tododer.ui.fragments.components.todo_list

import kanti.tododer.ui.state.TodoElement

data class TodoListUiState(
	val todoElements: List<TodoElement> = listOf(),
	val onItemClick: (TodoElement) -> Unit = {}
)