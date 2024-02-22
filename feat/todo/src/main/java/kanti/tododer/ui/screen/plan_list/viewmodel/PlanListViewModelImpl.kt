package kanti.tododer.ui.screen.plan_list.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.domain.plandeletebehaviour.DeletePlan
import kanti.tododer.domain.plandeletebehaviour.DeletePlanIfBlank
import kanti.tododer.domain.progress.computer.GetProgressFromAllPlan
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.plan.PlanData
import kanti.tododer.ui.components.plan.PlansData
import kanti.tododer.ui.services.deleter.DeleteCancelManager
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
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
	@ApplicationContext private val appContext: Context
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
		.map { plan ->
			PlanData(
				id = plan.id,
				title = appContext.getString(R.string.plan_all),
				progress = 0f
			)
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlanData()
		)
	override val planDefault: StateFlow<PlanData> = planRepository.planDefault
		.map { plan ->
			PlanData(
				id = plan.id,
				title = appContext.getString(R.string.plan_default),
				progress = 0f
			)
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlanData()
		)
	override val plans: StateFlow<PlansData> = planRepository.standardPlans
		.combine(deleteCancelManager.deletedValues) { plans, deletedPlans ->
			PlansData(
				plans = plans.map { plan ->
					PlanData(
						id = plan.id,
						title = plan.title,
						progress = 0f,
						visible = !deletedPlans.containsKey(plan.id)
					)
				}
			)
		}
		.flowOn(Dispatchers.Default)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlansData()
		)

	override val plansDeleted: SharedFlow<List<PlanData>> = deleteCancelManager.onDeleted
	override val blankPlanDeleted: SharedFlow<Unit> = deletePlanIfBlank.planDeleted

	override val planAllProgress: SharedFlow<Float> = getProgressFromAllPlan.planAllProgress
		.shareIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily
		)
	override val planDefaultProgress: SharedFlow<Float> = getProgressFromAllPlan.planDefaultProgress
		.shareIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily
		)
	override val plansProgress: SharedFlow<PlanProgress> = getProgressFromAllPlan.plansProgress
		.shareIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily
		)

	override fun updateUiState() {
		getProgressFromAllPlan.update()
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