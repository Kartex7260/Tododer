package kanti.tododer.ui.screen.plan_list.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.domain.plandeletebehaviour.DeletePlan
import kanti.tododer.domain.plandeletebehaviour.DeletePlanIfBlank
import kanti.tododer.domain.progress.computer.GetProgressFromAllPlan
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.common.PlansUiState
import kanti.tododer.ui.common.toUiState
import kanti.tododer.ui.components.plan.PlanData
import kanti.tododer.ui.components.selection.SelectionController
import kanti.tododer.ui.services.deleter.DeleteCancelManager
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlanListViewModelImpl @Inject constructor(
	private val planRepository: PlanRepository,
	private val deletePlan: DeletePlan,
	private val appDataRepository: AppDataRepository,
	private val getProgressFromAllPlan: GetProgressFromAllPlan,
	deletePlanIfBlank: DeletePlanIfBlank,
	@ApplicationContext private val appContext: Context,
	@StandardLog private val logger: Logger,
	private val selectionController: SelectionController
) : ViewModel(), PlanListViewModel {

	private val deleteCancelManager = DeleteCancelManager<PlanData>(
		toKey = { id },
		onDelete = { plans ->
			withContext(NonCancellable) {
				deletePlan(plans.map { it.id })
			}
		}
	)

	private val _newPlanCreated = MutableSharedFlow<Unit>()
	override val newPlanCreated: SharedFlow<Unit> = _newPlanCreated.asSharedFlow()

	override val planAll: StateFlow<PlanData> = planRepository.planAll
		.combine(getProgressFromAllPlan.planAllProgress) { plan, progress ->
			PlanData(
				id = plan.id,
				title = appContext.getString(R.string.plan_all),
				progress = progress
			)
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlanData()
		)
	override val planDefault: StateFlow<PlanData> = planRepository.planDefault
		.combine(getProgressFromAllPlan.planDefaultProgress) { plan, progress ->
			PlanData(
				id = plan.id,
				title = appContext.getString(R.string.plan_default),
				progress = progress
			)
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlanData()
		)
	override val plans: StateFlow<PlansUiState> = combine(
			flow = planRepository.standardPlans,
			flow2 = deleteCancelManager.deletedValues,
			flow3 = getProgressFromAllPlan.plansProgress,
			flow4 = selectionController.selectionState
		) { plans, deletedPlans, plansProgress, selectionState ->
			logger.d(LOG_TAG, "plans: combine(\n" +
					"\tplans=${plans.joinToString(
						prefix = "(ID, TITLE)[",
						postfix = "]"
					) { "(${it.id}, ${it.title})" }}\n" +
					"\tdeletedPlans=${deletedPlans.entries.joinToString(
						prefix = "(ID, TITLE)[",
						postfix = "]"
					) { "(${it.key}, ${it.value.title})" }}\n" +
					"\tplansProgress=${plansProgress.joinToString(
						prefix = "(ID, PROGRESS)[",
						postfix = "]"
					) { "(${it.planId}, ${it.progress})" }}\n" +
					"\tselectionState=${selectionState}\n)")
			PlansUiState(
				selection = selectionState.selection,
				plans = plans.map { plan ->
					plan.toUiState(
						selected = selectionState.selected.contains(plan.id),
						visible = !deletedPlans.containsKey(plan.id),
						progress = plansProgress.firstOrNull { it.planId == plan.id }?.progress ?: 0f
					)
				}
			)
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlansUiState()
		)

	override val plansDeleted: SharedFlow<List<PlanData>> = deleteCancelManager.onDeleted
	override val blankPlanDeleted: SharedFlow<Unit> = deletePlanIfBlank.planDeleted

	override val todosCount: StateFlow<Int> = getProgressFromAllPlan.totalTodos
		.onEach { logger.d(LOG_TAG, "getProgressFromAllPlan.totalTodos: collect=$it") }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = 0
		)

	init {
	    updateUiState()
	}

	override fun updateUiState() {
		viewModelScope.launch {
			getProgressFromAllPlan()
		}
	}

	override fun setCurrentPlan(planId: Long) {
		viewModelScope.launch(NonCancellable) {
			appDataRepository.setCurrentPlan(planId = planId)
		}
	}

	override fun createPlanEndSetCurrent(title: String) {
		viewModelScope.launch(NonCancellable) {
			val planId = planRepository.create(title)
			appDataRepository.setCurrentPlan(planId)
			_newPlanCreated.emit(Unit)
		}
	}

	override fun renamePlanTitle(todoId: Long, newTitle: String) {
		viewModelScope.launch {
			planRepository.updateTitle(todoId, newTitle)
		}
	}

	override fun deletePlans(plans: List<PlanData>) {
		viewModelScope.launch {
			deleteCancelManager.delete(plans)
		}
	}

	override fun deletePlan(planId: Long) {
		viewModelScope.launch {
			val plan = plans.value.plans.firstOrNull { it.data.id == planId } ?: return@launch
			deleteCancelManager.delete(listOf(plan.data))
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
			updateUiState()
		}
	}

	override fun selection(planId: Long) {
		selectionController.selection = true
		selectionController.setSelect(planId, true)
	}

	override fun selectionOff(): Boolean {
		if (selectionController.selection) {
			selectionController.clear()
			return true
		}
		return false
	}

	override fun setSelect(planId: Long, selected: Boolean) {
		selectionController.setSelect(planId, selected)
	}

	override fun deleteSelected() {
		viewModelScope.launch {
			val selected = selectionController.selected
			if (selected.isEmpty())
				return@launch
			selectionController.clear()
			val plans = plans.value.plans.filter { selected.contains(it.data.id) }
				.map { it.data }
			deletePlans(plans)
		}
	}

	companion object {

		private const val LOG_TAG = "PlanListViewModelImpl"
	}
}