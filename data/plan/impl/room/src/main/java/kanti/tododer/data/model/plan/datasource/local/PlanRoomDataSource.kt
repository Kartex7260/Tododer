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
	private val sl: StateLanguage
) : PlanLocalDataSource {

	private val logTag = "PlanRoomDataSource"

	override val planAll: Flow<Plan>
		get() = planDao.getFromTypeFlow(PlanType.All.name)
			.onEach {
				if (it == null) {
					Log.w(logTag, "Plan(ALl) not found!")
				}
			}
			.filterNotNull()
			.map { it.toPlan(sl) }
	override val defaultPlan: Flow<Plan>
		get() = planDao.getFromTypeFlow(PlanType.Default.name)
			.onEach {
				if (it == null) {
					Log.w(logTag, "Plan(Default) not found!")
				}
			}
			.filterNotNull()
			.map { it.toPlan(sl) }
	override val standardPlans: Flow<List<Plan>>
		get() = planDao.getAllPlansFlow(PlanState.Normal.name, PlanType.Custom.name).map { plans ->
			plans.map {
				it.toPlan(sl)
			}
		}

	override suspend fun getPlan(planId: Long): Plan? {
		return planDao.getPlan(planId)?.toPlan(sl)
	}

	override suspend fun getStandardPlans(): List<Plan> {
		return planDao.getAllPlans(
			state = PlanState.Normal.name,
			type = PlanType.Custom.name
		).map { it.toPlan(sl) }
	}

	override suspend fun getPlanFromType(type: PlanType): Plan {
		return when (type) {
			PlanType.Custom -> throw IllegalArgumentException("getPlanFromType for PlanType. All and Default only")
			else -> planDao.getFromType(type.name)?.toPlan(sl)
				?: throw IllegalStateException("Default data not initialized")
		}
	}

	override suspend fun getPlans(plansId: List<Long>): List<Plan> {
		return planDao.getAll(plansId).map { it.toPlan(sl) }
	}

	override suspend fun insert(plan: Plan): Long {
		val rowId = planDao.insert(plan.toPlanEntity(sl))
		if (rowId == -1L)
			throw IllegalArgumentException("Plan(id = ${plan.id}) already exist!")
		return rowId
	}

	override suspend fun insert(plans: List<Plan>) {
		planDao.insert(plans.map { it.toPlanEntity(sl) })
	}

	override suspend fun updateTitle(planId: Long, title: String) {
		planDao.updateTitle(planId, title)
	}

	override suspend fun delete(planIds: List<Long>) {
		planDao.delete(planIds)
	}

	override suspend fun deletePlanIfNameIsEmpty(planId: Long) {
		planDao.deleteIfNameEmpty(planId)
	}

	override suspend fun isEmpty(): Boolean {
		return planDao.count() == 0L
	}

	override suspend fun clear() {
		planDao.deleteAll()
	}
}