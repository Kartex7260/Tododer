package kanti.tododer.ui.screen.plan_list.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.plan.PlanData
import kanti.tododer.ui.components.plan.PlansData
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanListViewModelImpl @Inject constructor(
	private val planRepository: PlanRepository,
	private val appDataRepository: AppDataRepository,
	@ApplicationContext private val appContext: Context
) : ViewModel(), PlanListViewModel {

	private val _newPlanCreated = MutableSharedFlow<Unit>()
	override val newPlanCreated: SharedFlow<Unit> = _newPlanCreated.asSharedFlow()

	override val planAll: StateFlow<PlanData> = planRepository.planAll
		.map { plan ->
			PlanData(
				id = plan.id,
				title = appContext.getString(R.string.plan_all),
				progress = 0f
			)
		}.stateIn(
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
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlanData()
		)
	override val plans: StateFlow<PlansData> = planRepository.standardPlans
		.map { plans ->
			PlansData(
				plans = plans.map { plan ->
					PlanData(
						id = plan.id,
						title = plan.title,
						progress = 0f
					)
				}
			)
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = PlansData()
		)

	private val _planDeleted = MutableSharedFlow<String>()
	override val planDeleted: SharedFlow<String> = _planDeleted.asSharedFlow()

	override fun setCurrentPlan(planId: Int) {
		viewModelScope.launch {
			appDataRepository.setCurrentPlan(planId = planId)
		}
	}

	override fun createPlanEndSetCurrent(title: String) {
		viewModelScope.launch {
			val plan = planRepository.create(title)
			appDataRepository.setCurrentPlan(plan.id)
			_newPlanCreated.emit(Unit)
		}
	}

	override fun deletePlan(planId: Int) {
		viewModelScope.launch {
			val plans = planRepository.delete(listOf(planId))
			if (plans.isEmpty())
				return@launch
			_planDeleted.emit(plans[0].title)
		}
	}

	override fun undoDelete() {
		viewModelScope.launch {
			planRepository.undoDelete()
		}
	}

	override fun undoChanceRejected() {
		viewModelScope.launch {
			planRepository.undoChanceRejected()
		}
	}
}