package kanti.tododer.ui.screen.todo_list.viewmodel

import androidx.compose.runtime.Stable
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.ui.components.todo.TodosData

@Stable
data class TodoListUiState(
	val plan: Plan = Plan(),
	val children: TodosData = TodosData()
)
