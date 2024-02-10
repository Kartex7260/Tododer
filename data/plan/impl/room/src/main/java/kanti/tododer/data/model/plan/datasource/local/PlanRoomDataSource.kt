package kanti.tododer.data.model.plan.datasource.local

import kanti.sl.StateLanguage
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanState
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
    private val planDao: PlanDao,
    private val sl: StateLanguage,
    @StandardLog private val logger: Logger
) : PlanLocalDataSource {

    private val logTag = "PlanRoomDataSource"

    override val planAll: Flow<Plan?> = planDao.getFromTypeFlow(PlanType.All.name).map {
        logger.d(logTag, "planAll: Flow<Plan?>: collect $it")
        it?.toPlan(sl)
    }.flowOn(Dispatchers.IO)

    override val defaultPlan: Flow<Plan?> = planDao.getFromTypeFlow(PlanType.Default.name).map {
        logger.d(logTag, "defaultPlan: Flow<Plan?>: collect $it")
        it?.toPlan(sl)
    }.flowOn(Dispatchers.IO)

    override val standardPlans: Flow<List<Plan>>
        get() = planDao.getAllPlansFlow(PlanState.Normal.name, PlanType.Custom.name).map { plans ->
            plans.map {
                it.toPlan(sl)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getPlan(planId: Long): Plan? {
        return withContext(Dispatchers.IO) {
            planDao.getPlan(planId)?.toPlan(sl)
        }
    }

    override suspend fun getStandardPlans(): List<Plan> {
        return withContext(Dispatchers.IO) {
            planDao.getAllPlans(
                state = PlanState.Normal.name, type = PlanType.Custom.name
            ).map { it.toPlan(sl) }
        }
    }

    override suspend fun getPlanFromType(type: PlanType): Plan? {
        return when (type) {
            PlanType.Custom ->
                throw IllegalArgumentException("getPlanFromType for PlanType. All and Default only")
            else -> withContext(Dispatchers.IO) {
                planDao.getFromType(type.name)?.toPlan(sl)
            }
        }
    }

    override suspend fun getPlans(plansId: List<Long>): List<Plan> {
        return withContext(Dispatchers.IO) {
            planDao.getAll(plansId).map { it.toPlan(sl) }
        }
    }

    override suspend fun insert(plan: Plan): Long {
        return withContext(Dispatchers.IO) {
            val rowId = planDao.insert(plan.toPlanEntity(sl))
            if (rowId == -1L) throw IllegalArgumentException("Plan(id = ${plan.id}) already exist!")
            rowId
        }
    }

    override suspend fun insert(plans: List<Plan>) {
        withContext(Dispatchers.IO) {
            planDao.insert(plans.map { it.toPlanEntity(sl) })
        }
    }

    override suspend fun updateTitle(planId: Long, title: String) {
        planDao.updateTitle(planId, title)
    }

    override suspend fun delete(planIds: List<Long>) {
        planDao.delete(planIds)
    }

    override suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean {
        return planDao.deleteIfNameEmpty(planId) == 1
    }

    override suspend fun isEmpty(): Boolean {
        return planDao.count() == 0L
    }

    override suspend fun clear() {
        planDao.deleteAll()
    }
}