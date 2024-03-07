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
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.domain.plandeletebehaviour.DeletePlanIfBlank
import kanti.tododer.domain.todo.delete.DeleteBlankTodoWithFlow
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.common.GroupUiState
import kanti.tododer.ui.common.TodoDataWithGroup
import kanti.tododer.ui.common.TodosUiState
import kanti.tododer.ui.common.toData
import kanti.tododer.ui.common.toUiState
import kanti.tododer.ui.components.grouping.GroupExpandingController
import kanti.tododer.ui.components.selection.SelectionController
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
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
	private val selectionController: SelectionController,
	private val groupExpandingController: GroupExpandingController,
	@ApplicationContext context: Context,
	@StandardLog private val logger: Logger
) : ViewModel(), TodoListViewModel {

	private val deleteCancelManager = DeleteCancelManager<TodoDeletion>(
		toKey = { todoData.id },
		onDelete = { todos ->
			todoRepository.delete(todos.map { it.todoData.id })
			updateUiState.value = Any()
		}
	)

	private val updateUiState = MutableStateFlow(Any())
	override val currentPlan: StateFlow<TodoListUiState> = appDataRepository.currentPlanId
		.onEach {
			selectionController.clear()
			groupExpandingController.clear()
		}
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
		.run {
			combine(
				flow = this,
				flow2 = deleteCancelManager.deletedValues,
				flow3 = selectionController.selectionState
			) { planWithChildren, deletedChildren, selectionState ->
				logger.d(
					LOG_TAG,
					"currentPlan: combine($planWithChildren, $deletedChildren, $selectionState)"
				)
				TodoListUiState(
					plan = planWithChildren.first,
					children = TodosUiState(
						selection = selectionState.selection,
						groups = planWithChildren.second
							.groupBy { it.group }.entries.asFlow()
							.map { groupWithTodos ->
								GroupUiState(
									name = groupWithTodos.key,
									expand = groupExpandingController.visit(groupWithTodos.key),
									todos = groupWithTodos.value.map { todo ->
										todo.toUiState(
											selected = selectionState.selected.contains(todo.id),
											visible = !deletedChildren.containsKey(todo.id)
										)
									}
								)
							}
							.toList()
							.sortedWith(comparator = GroupUiState.COMPARATOR)
					)
				)
			}
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = TodoListUiState()
		)

	private val _todosDeleted = MutableSharedFlow<List<TodoDeletion>>()
	override val todosDeleted: SharedFlow<List<TodoDeletion>> = _todosDeleted.asSharedFlow()

	private val _blankTodoDeleted = MutableSharedFlow<Unit>()
	override val blankTodoDeleted: SharedFlow<Unit> = _blankTodoDeleted.asSharedFlow()

	private val _goToTodo = MutableSharedFlow<Long>()
	override val goToTodo: SharedFlow<Long> = _goToTodo.asSharedFlow()

	private val _selectionGrouping = MutableSharedFlow<List<TodoDataWithGroup>>()
	override val groupingSelection = _selectionGrouping.asSharedFlow()

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
			val deletedTodoData = listOf(TodoDeletion(deletedTodo.toData(), true))
			deleteCancelManager.delete(deletedTodoData)
			_todosDeleted.emit(deletedTodoData)
		}
	}

	override fun createNewTodo(title: String, goTo: Boolean) {
		viewModelScope.launch {
			val planFullId = if (currentPlan.value.plan.type == PlanType.All) {
				FullId(id = Const.PlansIds.DEFAULT, FullIdType.Plan)
			} else {
				currentPlan.value.plan.toFullId()
			}
			val todoId = todoRepository.create(planFullId, title, "")
			if (goTo) {
				_goToTodo.emit(todoId)
			}
			updateUiState.value = Any()
		}
	}

	override fun setGroup(todoIds: List<Long>, group: String?) {
		viewModelScope.launch {
			todoRepository.setGroup(todoIds, group)
			updateUiState.value = Any()
		}
	}

	override fun setGroupExpand(group: String?, expand: Boolean) {
		groupExpandingController.setExpand(group, expand)
		updateUiState.value = Any()
	}

	override fun renamePlan(newTitle: String) {
		viewModelScope.launch {
			val plan = currentPlan.value.plan
			planRepository.updateTitle(planId = plan.id, title = newTitle)
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
			val todoDeletions = todos.map { TodoDeletion(it, false) }
			deleteCancelManager.delete(todoDeletions)
			_todosDeleted.emit(todoDeletions)
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

	override fun switchSelection() {
		selectionController.switchSelection()
	}

	override fun selection(todoId: Long) {
		selectionController.selection = true
		if (todoId != 0L) {
			selectionController.setSelect(todoId, true)
		}
	}

	override fun groupingSelection() {
		viewModelScope.launch {
			val selected = selectionController.selected
			val todos = currentPlan.value.children.groups.asSequence()
				.flatMap { groupUiState ->
					groupUiState.todos.map { todoUiState ->
						TodoDataWithGroup(todoData = todoUiState.data, group = groupUiState.name)
					}
				}
				.filter { selected.contains(it.todoData.id) }
			_selectionGrouping.emit(todos.toList())
		}
	}

	override fun selectionOff(): Boolean {
		if (selectionController.selection) {
			selectionController.clear()
			return true
		}
		return false
	}

	override fun setSelect(todoId: Long, selected: Boolean) {
		selectionController.setSelect(todoId, selected)
	}

	override fun setSelect(group: String?, selected: Boolean) {
		viewModelScope.launch {
			val groupUiState = currentPlan.value.children.groups
				.firstOrNull { it.name == group } ?: return@launch
			selectionController.setSelect(
				ids = groupUiState.todos.map { it.data.id },
				select = selected
			)
		}
	}

	override fun changeDoneSelected() {
		viewModelScope.launch {
			val selected = selectionController.selected
			if (selected.isEmpty())
				return@launch
			val todosDone = currentPlan.value.children.groups
				.flatMap { it.todos }
				.filter { selected.contains(it.data.id) }
				.map { it.data }
			val totalDone = todosDone.fold(true) { acc, todoData -> acc and todoData.isDone }
			todoRepository.changeDone(
				todoIds = selected,
				isDone = !totalDone
			)
			updateUiState.value = Any()
		}
	}

	override fun deleteSelected() {
		viewModelScope.launch {
			val selected = selectionController.selected
			if (selected.isEmpty())
				return@launch
			selectionController.clear()
			val todos = currentPlan.value.children.groups
				.flatMap { it.todos }
				.filter { selected.contains(it.data.id) }
				.map { it.data }
			deleteTodos(todos)
		}
	}

	override fun onStop() {
		rejectCancelChance()
		viewModelScope.launch {
			val planFullId = FullId(currentPlan.value.plan.id, FullIdType.Plan)
			deletePlanIfBlank(planFullId)
		}
	}

	companion object {

		private const val LOG_TAG = "TodoListViewModelImpl"
	}
}