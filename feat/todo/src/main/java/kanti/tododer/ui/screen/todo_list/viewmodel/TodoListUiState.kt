package kanti.tododer.ui.screen.todo_list.viewmodel

import androidx.compose.runtime.Stable
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.common.TodosUiState

@Stable
data class TodoListUiState(
	val plan: Plan = Plan(),
	val isEditablePlan: Boolean = plan.type.isEditable,
	val children: TodosUiState = TodosUiState()
)
