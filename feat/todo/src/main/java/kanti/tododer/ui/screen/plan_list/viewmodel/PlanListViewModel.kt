package kanti.tododer.ui.screen.plan_list.viewmodel

import kanti.tododer.ui.components.plan.PlanUiState
import kanti.tododer.ui.components.plan.PlansUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface PlanListViewModel {

	val newPlanCreated: SharedFlow<Unit>

	val planAll: StateFlow<PlanUiState>
	val planDefault: StateFlow<PlanUiState>
	val plans: StateFlow<PlansUiState>

	fun setCurrentPlan(planId: Int)

	fun createPlanEndSetCurrent(title: String)

	companion object : PlanListViewModel {

		private val coroutineScope = CoroutineScope(Dispatchers.Default)

		private val _newPlanCreated = MutableSharedFlow<Unit>()
		private val _planAll = MutableStateFlow(PlanUiState(-1, "All", .01f))
		private val _planDefault = MutableStateFlow(PlanUiState(-2, "Default", .5f))
		private val _plans = MutableStateFlow(PlansUiState(listOf(
			PlanUiState(1, "Test", .2f),
			PlanUiState(2, "Work", .9f)
		)))

		override val newPlanCreated: SharedFlow<Unit> = _newPlanCreated.asSharedFlow()
		override val planAll: StateFlow<PlanUiState> = _planAll.asStateFlow()
		override val planDefault: StateFlow<PlanUiState> = _planDefault.asStateFlow()
		override val plans: StateFlow<PlansUiState> = _plans.asStateFlow()

		override fun setCurrentPlan(planId: Int) {
		}

		override fun createPlanEndSetCurrent(title: String) {
			coroutineScope.launch {
				_newPlanCreated.emit(Unit)
			}
		}
	}
}