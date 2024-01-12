package kanti.tododer.ui.screen.plan_list.viewmodel

import android.util.Log
import kanti.tododer.ui.components.plan.PlanData
import kanti.tododer.ui.components.plan.PlansData
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

	val planAll: StateFlow<PlanData>
	val planDefault: StateFlow<PlanData>
	val plans: StateFlow<PlansData>

	fun setCurrentPlan(planId: Int)

	fun createPlanEndSetCurrent(title: String)

	fun deletePlans(planIds: List<Int>)

	companion object : PlanListViewModel {

		private const val logTag = "PlanListViewModel"

		private val coroutineScope = CoroutineScope(Dispatchers.Default)

		private val _newPlanCreated = MutableSharedFlow<Unit>()
		override val newPlanCreated: SharedFlow<Unit> = _newPlanCreated.asSharedFlow()

		private val _planAll = MutableStateFlow(PlanData(-1, "All", .01f))
		private val _planDefault = MutableStateFlow(PlanData(-2, "Default", .5f))
		private val _plans = MutableStateFlow(PlansData(listOf(
			PlanData(id = 1, title = "Test", progress = .2f),
			PlanData(id = 2, title = "Work", progress = .9f),
			PlanData(id = 3, title = "Test 1"),
			PlanData(id = 4, title = "Test 2"),
			PlanData(id = 5, title = "Test 3"),
			PlanData(id = 6, title = "Test 4"),
			PlanData(id = 7, title = "Test 5"),
			PlanData(id = 8, title = "Test 6"),
			PlanData(id = 9, title = "Test 7"),
			PlanData(id = 10, title = "Test 8"),
			PlanData(id = 11, title = "Test 9"),
			PlanData(id = 12, title = "Test 10"),
			PlanData(id = 13, title = "Test 11"),
			PlanData(id = 4, title = "Test 12")
		)))

		override val planAll: StateFlow<PlanData> = _planAll.asStateFlow()
		override val planDefault: StateFlow<PlanData> = _planDefault.asStateFlow()
		override val plans: StateFlow<PlansData> = _plans.asStateFlow()

		override fun setCurrentPlan(planId: Int) {
			Log.d(logTag, "setCurrentPlan(planId: Int = $planId)")
		}

		override fun createPlanEndSetCurrent(title: String) {
			Log.d(logTag, "createPlanEndSetCurrent(title: String = $title)")
			coroutineScope.launch {
				_newPlanCreated.emit(Unit)
			}
		}

		override fun deletePlans(planIds: List<Int>) {
			Log.d(logTag, "deletePlan(planId: Int = ${planIds.firstOrNull()})")
		}
	}
}