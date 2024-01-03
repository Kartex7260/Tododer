package kanti.tododer.ui.screen.todo_list.viewmodel

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.components.todo.TodosUiState

sealed class TodoListUiState {

	data object Empty : TodoListUiState()

	data class Success(
		val plan: Plan,
		val children: TodosUiState
	) : TodoListUiState()

	data class Fail(
		val message: String,
		val th: Throwable? = null
	) : TodoListUiState()
}
