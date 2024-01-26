package kanti.tododer.ui.screen.todo_list.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.tododer.common.Const
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toFullId
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.toTodoData
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.domain.plandeletebehaviour.DeletePlanIfBlank
import kanti.tododer.domain.todo.delete.DeleteBlankTodoWithFlow
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodosData
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
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
	private val deletePlanIfBlank: DeletePlanIfBlank,
	deleteBlankTodoWithFlow: DeleteBlankTodoWithFlow,
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
			TodoListUiState(
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
			initialValue = TodoListUiState()
		)

	private val _todosDeleted = MutableSharedFlow<List<TodoData>>()
	override val todosDeleted: SharedFlow<List<TodoData>> = _todosDeleted.asSharedFlow()

	private val _blankTodoDeleted = MutableSharedFlow<Unit>()
	override val blankTodoDeleted: SharedFlow<Unit> = _blankTodoDeleted.asSharedFlow()

	private val _newTodoCreated = MutableSharedFlow<Long>()
	override val newTodoCreated: SharedFlow<Long> = _newTodoCreated.asSharedFlow()

	init {
		viewModelScope.launch {
			deleteBlankTodoWithFlow.blankTodoDeleted.collectLatest {
				delay(100L)
				_blankTodoDeleted.emit(Unit)
			}
		}
	}

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
			val planFullId = if (currentPlan.value.plan.type == PlanType.All) {
				FullId(id = Const.PlansIds.DEFAULT, FullIdType.Plan)
			} else {
				currentPlan.value.plan.toFullId()
			}
			val todoId = todoRepository.create(planFullId, "", "")
			_newTodoCreated.emit(todoId)
			updateUiState.value = Any()
		}
	}

	override fun renameTodo(todoId: Long, newTitle: String) {
		viewModelScope.launch {
			todoRepository.updateTitle(todoId, newTitle)
			updateUiState.value = Any()
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

	override fun onStop() {
		rejectCancelChance()
		viewModelScope.launch {
			val planFullId = FullId(currentPlan.value.plan.id, FullIdType.Plan)
			deletePlanIfBlank.invoke(planFullId)
		}
	}
}