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

    override val planDefault: Flow<Plan?> = planDao.getFromTypeFlow(PlanType.Default.name).map {
        logger.d(logTag, "defaultPlan: Flow<Plan?>: collect $it")
        it?.toPlan(sl)
    }.flowOn(Dispatchers.IO)

    override val standardPlans: Flow<List<Plan>> =
        planDao.getAllPlansFlow(PlanState.Normal.name, PlanType.Custom.name).map { plans ->
            logger.d(logTag, "standardPlans: Flow<List<Plan>>: collect count(${plans.size})")
            plans.map {
                it.toPlan(sl)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getPlan(planId: Long): Plan? {
        return withContext(Dispatchers.IO) {
            val result = planDao.getPlan(planId)?.toPlan(sl)
            logger.d(logTag, "getPlan(Long = $planId): return $result")
            result
        }
    }

    override suspend fun getPlanFromType(type: PlanType): Plan? {
        val result = when (type) {
            PlanType.Custom -> { null }
            else -> withContext(Dispatchers.IO) { planDao.getFromType(type.name)?.toPlan(sl) }
        }
        logger.d(logTag, "getPlanFromType(PlanType = $type): return $result")
        return result
    }

    override suspend fun getStandardPlans(): List<Plan> {
        return withContext(Dispatchers.IO) {
            val plans = planDao.getAllPlans(
                state = PlanState.Normal.name, type = PlanType.Custom.name
            ).map { it.toPlan(sl) }
            logger.d(logTag, "getStandardPlans(): return count(${plans.size})")
            plans
        }
    }

    override suspend fun getPlans(plansId: List<Long>): List<Plan> {
        return withContext(Dispatchers.IO) {
            val result = planDao.getAll(plansId).map { it.toPlan(sl) }
            logger.d(
                logTag,
                "getPlans(List<Long> = count(${plansId.size})): return count(${result.size})"
            )
            result
        }
    }

    override suspend fun insert(plan: Plan): Long {
        return withContext(Dispatchers.IO) {
            val rowId = planDao.insert(plan.toPlanEntity(sl))
            logger.d(logTag, "insert(Plan = $plan): return $rowId")
            rowId
        }
    }

    override suspend fun insert(plans: List<Plan>) {
        withContext(Dispatchers.IO) {
            logger.d(logTag, "insert(List<Plan> = count(${plans.size}))")
            planDao.insert(plans.map { it.toPlanEntity(sl) })
        }
    }

    override suspend fun updateTitle(planId: Long, title: String) {
        logger.d(logTag, "updateTitle(Long = $planId, String = $title)")
        planDao.updateTitle(planId, title)
    }

    override suspend fun delete(planIds: List<Long>) {
        logger.d(logTag, "delete(List<Long> = count(${planIds.size}))")
        planDao.delete(planIds)
    }

    override suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean {
        val result = planDao.deleteIfNameEmpty(planId, PlanType.Custom.name) == 1
        logger.d(logTag, "deletePlanIfNameIsEmpty(Long = $planId): return $result")
        return result
    }

    override suspend fun isEmpty(): Boolean {
        val result = planDao.count() == 0L
        logger.d(logTag, "isEmpty(): return $result")
        return result
    }

    override suspend fun clear() {
        logger.d(logTag, "clear()")
        planDao.deleteAll()
    }
}