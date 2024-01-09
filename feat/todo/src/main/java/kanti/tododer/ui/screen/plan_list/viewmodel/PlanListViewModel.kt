package kanti.tododer.ui.screen.plan_list.viewmodel

import android.util.Log
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

	val planDeleted: SharedFlow<String>

	fun setCurrentPlan(planId: Int)

	fun createPlanEndSetCurrent(title: String)

	fun deletePlan(planId: Int)

	fun undoDelete()

	companion object : PlanListViewModel {

		private const val logTag = "PlanListViewModel"

		private val coroutineScope = CoroutineScope(Dispatchers.Default)

		private val _newPlanCreated = MutableSharedFlow<Unit>()
		override val newPlanCreated: SharedFlow<Unit> = _newPlanCreated.asSharedFlow()

		private val _planAll = MutableStateFlow(PlanUiState(-1, "All", .01f))
		private val _planDefault = MutableStateFlow(PlanUiState(-2, "Default", .5f))
		private val _plans = MutableStateFlow(PlansUiState(listOf(
			PlanUiState(id = 1, title = "Test", progress = .2f),
			PlanUiState(id = 2, title = "Work", progress = .9f),
			PlanUiState(id = 3, title = "Test 1"),
			PlanUiState(id = 4, title = "Test 2"),
			PlanUiState(id = 5, title = "Test 3"),
			PlanUiState(id = 6, title = "Test 4"),
			PlanUiState(id = 7, title = "Test 5"),
			PlanUiState(id = 8, title = "Test 6"),
			PlanUiState(id = 9, title = "Test 7"),
			PlanUiState(id = 10, title = "Test 8"),
			PlanUiState(id = 11, title = "Test 9"),
			PlanUiState(id = 12, title = "Test 10"),
			PlanUiState(id = 13, title = "Test 11"),
			PlanUiState(id = 4, title = "Test 12")
		)))

		override val planAll: StateFlow<PlanUiState> = _planAll.asStateFlow()
		override val planDefault: StateFlow<PlanUiState> = _planDefault.asStateFlow()
		override val plans: StateFlow<PlansUiState> = _plans.asStateFlow()

		private val _planDeleted = MutableSharedFlow<String>()
		override val planDeleted: SharedFlow<String> = _planDeleted.asSharedFlow()

		override fun setCurrentPlan(planId: Int) {
			Log.d(logTag, "setCurrentPlan(planId: Int = $planId)")
		}

		override fun createPlanEndSetCurrent(title: String) {
			Log.d(logTag, "createPlanEndSetCurrent(title: String = $title)")
			coroutineScope.launch {
				_newPlanCreated.emit(Unit)
			}
		}

		override fun deletePlan(planId: Int) {
			Log.d(logTag, "deletePlan(planId: Int = $planId)")
			coroutineScope.launch {
				_planDeleted.emit("Test deleted")
			}
		}

		override fun undoDelete() {
			Log.d(logTag, "undoDelete()")
		}
	}
}