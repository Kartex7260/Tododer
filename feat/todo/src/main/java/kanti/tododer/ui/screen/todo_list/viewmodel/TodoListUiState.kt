package kanti.tododer.ui.screen.todo_list.viewmodel

import androidx.compose.runtime.Stable
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.components.todo.TodosUiState

sealed class TodoListUiState {

	data object Empty : TodoListUiState()

	@Stable
	data class Success(
		val plan: Plan,
		val children: TodosUiState
	) : TodoListUiState()

	@Stable
	data class Fail(
		val message: String,
		val th: Throwable? = null
	) : TodoListUiState()
}
