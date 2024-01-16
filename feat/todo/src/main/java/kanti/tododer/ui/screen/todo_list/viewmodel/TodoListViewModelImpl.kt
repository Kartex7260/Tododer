package kanti.tododer.ui.screen.todo_list.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.tododer.Const
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toFullId
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toTodoData
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModelImpl @Inject constructor(
	appDataRepository: AppDataRepository,
	private val todoRepository: TodoRepository,
	private val planRepository: PlanRepository,
	private val getPlanChildren: GetPlanChildren,
	@ApplicationContext context: Context
) : ViewModel(), TodoListViewModel {

	private val deleteCancelManager = DeleteCancelManager<TodoData>(
		toKey = { id },
		onDelete = { todos ->
			todoRepository.delete(todos.map { it.id })
			updateUiState.value = Any()
		}
	)

	private val updateUiState = MutableStateFlow(Any())
	override val currentPlan: StateFlow<TodoListUiState> = appDataRepository.currentPlanId
		.combine(updateUiState) { planId, _ -> planId }
		.map { currentPlanId ->
			var plan = planRepository.getPlanOrDefault(currentPlanId)
			plan = when (plan.type) {
				PlanType.All -> plan.toPlan(title = context.getString(R.string.plan_all))
				PlanType.Default -> plan.toPlan(title = context.getString(R.string.plan_default))
				else -> plan
			}
			val children = getPlanChildren(plan.id)

			Pair(
				first = plan,
				second = children
			)
		}
		.combine(deleteCancelManager.deletedValues) { planWithChildren, deletedChildren ->
			Pair(
				first = planWithChildren.first,
				second = planWithChildren.second.filter { todo ->
					!deletedChildren.containsKey(todo.id)
				}
			)
		}
		.map { planWithChildren ->
			TodoListUiState.Success(
				plan = planWithChildren.first,
				children = TodosData(
					todos = planWithChildren.second.map { todo ->
						TodoData(
							id = todo.id,
							title = todo.title,
							remark = todo.remark,
							isDone = todo.done
						)
					}
				)
			)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodoListUiState.Empty
		)

	private val _todosDeleted = MutableSharedFlow<List<TodoData>>()
	override val todosDeleted: SharedFlow<List<TodoData>> = _todosDeleted.asSharedFlow()

	private val _newTodoCreated = MutableSharedFlow<Long>()
	override val newTodoCreated: SharedFlow<Long> = _newTodoCreated.asSharedFlow()

	override fun updateUiState(deletedTodoId: Long?) {
		updateUiState.value = Any()

		if (deletedTodoId == null)
			return
		viewModelScope.launch {
			val deletedTodo = todoRepository.getTodo(deletedTodoId) ?: return@launch
			val deletedTodoData = listOf(deletedTodo.toTodoData())
			deleteCancelManager.delete(deletedTodoData)
			_todosDeleted.emit(deletedTodoData)
		}
	}

	override fun createNewTodo() {
		viewModelScope.launch {
			when (val curPlan = currentPlan.value) {
				is TodoListUiState.Success -> {
					val todoId = todoRepository.create(curPlan.plan.toFullId(), "", "")
					_newTodoCreated.emit(todoId)
					updateUiState.value = Any()
				}
				else -> {}
			}
		}
	}

	override fun changeDone(todoId: Long, isDone: Boolean) {
		viewModelScope.launch {
			todoRepository.changeDone(todoId, isDone)
			updateUiState.value = Any()
		}
	}

	override fun deleteTodos(todos: List<TodoData>) {
		if (todos.isEmpty())
			return
		viewModelScope.launch {
			deleteCancelManager.delete(todos)
			_todosDeleted.emit(todos)
		}
	}

	override fun cancelDelete() {
		viewModelScope.launch {
			deleteCancelManager.cancelDelete()
		}
	}

	override fun rejectCancelChance() {
		viewModelScope.launch {
			deleteCancelManager.rejectCancelChance()
		}
	}
}