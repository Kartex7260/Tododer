package kanti.tododer.ui.screen.todo_list.viewmodel

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.ui.components.todo.TodosUiState
import kanti.tododer.ui.components.todo.TodoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TodoListViewModel {

	val currentPlan: StateFlow<TodoListUiState>

	fun changeDone(todoId: Int, isDone: Boolean)

	companion object : TodoListViewModel {

		private val _children = MutableStateFlow<TodoListUiState>(TodoListUiState.Success(
			plan = Plan(1, title = "Test", type = PlanType.Custom),
			children = TodosUiState(listOf(
				TodoUiState(1, "Test 1", false),
				TodoUiState(2, "Test 2", false),
				TodoUiState(3, "Test 3", false),
				TodoUiState(4, "Test 4", false),
				TodoUiState(5, "Test 5", false),
				TodoUiState(6, "Test 6", false),
				TodoUiState(7, "Test 7", false),
				TodoUiState(8, "Test 8", false),
				TodoUiState(9, "Test 9", false),
				TodoUiState(10, "Test 10", false),
				TodoUiState(11, "Test 11", false)
			))
		))
		override val currentPlan = _children.asStateFlow()

		override fun changeDone(todoId: Int, isDone: Boolean) {
		}
	}
}