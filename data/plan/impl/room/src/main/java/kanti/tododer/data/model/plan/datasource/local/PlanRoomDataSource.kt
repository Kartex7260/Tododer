package kanti.tododer.data.model.plan.datasource.local

import android.util.Log
import kanti.sl.StateLanguage
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanState
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.room.plan.PlanDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	private val planDao: PlanDao,
	private val initializer: PlanInitializer,
	private val sl: StateLanguage
) : PlanLocalDataSource {

	private val logTag = "PlanRoomDataSource"

	override val planAll: Flow<Plan>
		get() = planDao.getFromType(PlanType.All.name)
			.onEach {
				if (it == null) {
					Log.w(logTag, "Plan(ALl) not found!")
				}
			}
			.filterNotNull()
			.map { it.toPlan(sl) }
	override val defaultPlan: Flow<Plan>
		get() = planDao.getFromType(PlanType.Default.name)
			.onEach {
				if (it == null) {
					Log.w(logTag, "Plan(Default) not found!")
				}
			}
			.filterNotNull()
			.map { it.toPlan(sl) }
	override val standardPlans: Flow<List<Plan>>
		get() = planDao.getAll(PlanState.Normal.name, PlanType.Custom.name).map { plans ->
			plans.map {
				it.toPlan(sl)
			}
		}

	override suspend fun getPlans(plansId: List<Int>): List<Plan> {
		return planDao.getAll(plansId).map { it.toPlan(sl) }
	}

	override suspend fun insert(plan: Plan): Plan {
		val rowId = planDao.insert(plan.toPlanEntity(sl))
		if (rowId == -1L)
			throw IllegalArgumentException("Plan(id = ${plan.id}) already exist!")
		return planDao.getByRowId(rowId)?.toPlan(sl)
			?: throw IllegalStateException("Not found plan by rowId=$rowId")
	}

	override suspend fun insert(plans: List<Plan>) {
		planDao.insert(plans.map { it.toPlanEntity(sl) })
	}

	override suspend fun updateTitle(planId: Int, title: String): Plan {
		planDao.updateTitle(planId, title)
		return planDao.getPlan(planId)?.toPlan(sl)
			?:throw IllegalArgumentException("Not found plan by id=$planId")
	}

	override suspend fun delete(planIds: List<Int>) {
		planDao.delete(planIds)
	}

	override suspend fun init() {
		Log.d(logTag, "init()")
		if (isEmpty()) {
			Log.d(logTag, "db is empty")
			initializer.initialize(this)
		}
	}

	override suspend fun isEmpty(): Boolean {
		return planDao.count() == 0
	}

	override suspend fun clear() {
		planDao.deleteAll()
		initializer.initialize(this)
	}
}