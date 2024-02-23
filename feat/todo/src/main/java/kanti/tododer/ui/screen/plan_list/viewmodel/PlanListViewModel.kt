package kanti.tododer.ui.screen.plan_list.viewmodel

import android.util.Log
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.ui.common.PlanUiState
import kanti.tododer.ui.common.PlansUiState
import kanti.tododer.ui.components.plan.PlanData
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
    val plans: StateFlow<PlansUiState>

    val plansDeleted: SharedFlow<List<PlanData>>
    val blankPlanDeleted: SharedFlow<Unit>

    val planAllProgress: SharedFlow<Float>
    val planDefaultProgress: SharedFlow<Float>
    val plansProgress: SharedFlow<PlanProgress>

    fun updateUiState()

    fun setCurrentPlan(planId: Long)

    fun createPlanEndSetCurrent(title: String)

    fun renamePlanTitle(todoId: Long, newTitle: String)

    fun deletePlans(plans: List<PlanData>)

    fun deletePlan(planId: Long)

    fun cancelDelete()

    fun rejectCancelChance()

    companion object : PlanListViewModel {

        private const val logTag = "PlanListViewModel"

        private val coroutineScope = CoroutineScope(Dispatchers.Default)

        private val _newPlanCreated = MutableSharedFlow<Unit>()
        override val newPlanCreated: SharedFlow<Unit> = _newPlanCreated.asSharedFlow()

        private val _planAll = MutableStateFlow(PlanData(-1, "All", .01f))
        private val _planDefault = MutableStateFlow(PlanData(-2, "Default", .5f))
        private val _plans = MutableStateFlow(
            PlansUiState(
                plans = listOf(
                    PlanUiState(data = PlanData(id = 1, title = "Test", progress = .2f)),
                    PlanUiState(data = PlanData(id = 2, title = "Work", progress = .9f)),
                    PlanUiState(data = PlanData(id = 3, title = "Test 1")),
                    PlanUiState(data = PlanData(id = 4, title = "Test 2")),
                    PlanUiState(data = PlanData(id = 5, title = "Test 3")),
                    PlanUiState(data = PlanData(id = 6, title = "Test 4")),
                    PlanUiState(data = PlanData(id = 7, title = "Test 5")),
                    PlanUiState(data = PlanData(id = 8, title = "Test 6")),
                    PlanUiState(data = PlanData(id = 9, title = "Test 7")),
                    PlanUiState(data = PlanData(id = 10, title = "Test 8")),
                    PlanUiState(data = PlanData(id = 11, title = "Test 9")),
                    PlanUiState(data = PlanData(id = 12, title = "Test 10")),
                    PlanUiState(data = PlanData(id = 13, title = "Test 11")),
                    PlanUiState(data = PlanData(id = 4, title = "Test 12"))
                )
            )
        )

        override val planAll: StateFlow<PlanData> = _planAll.asStateFlow()
        override val planDefault: StateFlow<PlanData> = _planDefault.asStateFlow()
        override val plans: StateFlow<PlansUiState> = _plans.asStateFlow()

        private val _plansDeleted = MutableSharedFlow<List<PlanData>>()
        override val plansDeleted: SharedFlow<List<PlanData>> = _plansDeleted.asSharedFlow()

        private val _blankPlanDeleted = MutableSharedFlow<Unit>()
        override val blankPlanDeleted: SharedFlow<Unit> = _blankPlanDeleted.asSharedFlow()

        override val planAllProgress: SharedFlow<Float>
            get() = MutableSharedFlow()
        override val planDefaultProgress: SharedFlow<Float>
            get() = MutableSharedFlow()
        override val plansProgress: SharedFlow<PlanProgress>
            get() = MutableSharedFlow()

        override fun updateUiState() {}

        override fun setCurrentPlan(planId: Long) {
            Log.d(logTag, "setCurrentPlan(planId: Int = $planId)")
        }

        override fun createPlanEndSetCurrent(title: String) {
            Log.d(logTag, "createPlanEndSetCurrent(title: String = $title)")
            coroutineScope.launch {
                _newPlanCreated.emit(Unit)
            }
        }

        override fun renamePlanTitle(todoId: Long, newTitle: String) {
        }

        override fun deletePlans(plans: List<PlanData>) {
            Log.d(logTag, "deletePlan(planId: Int = ${plans.firstOrNull()})")
            coroutineScope.launch {
                _plansDeleted.emit(plans)
            }
        }

        override fun deletePlan(planId: Long) {
        }

        override fun cancelDelete() {
        }

        override fun rejectCancelChance() {
        }
    }
}