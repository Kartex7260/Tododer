package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val localDataSource: PlanLocalDataSource,
    @StandardLog private val logger: Logger
) : PlanRepository {

    private val logTag = "PlanRepositoryImpl"

    override val planAll: Flow<Plan> = localDataSource.planAll
        .onEach { plan ->
            logger.d(logTag, "planAll: Flow<Plan> collect $plan")
            if (plan == null) {
                logger.d(logTag, "planAll: Flow<Plan> it collect is ignore")
                localDataSource.insert(PlanAll)
            }
        }
        .filterNotNull()

    override val planDefault: Flow<Plan> = localDataSource.planDefault
        .onEach { plan ->
            logger.d(logTag, "planAll: Flow<Plan> collect $plan")
            if (plan == null) {
                logger.d(logTag, "planAll: Flow<Plan> it collect is ignore")
                localDataSource.insert(PlanDefault)
            }
        }
        .filterNotNull()

    override val standardPlans: Flow<List<Plan>> = localDataSource.standardPlans
        .onEach {
            logger.d(logTag, "standardPlans: Flow<List<Plan>>: collect count(${it.size})")
        }

    override suspend fun getDefaultPlan(): Plan {
        var plan = localDataSource.getPlanFromType(PlanType.Default)
        if (plan == null) {
            localDataSource.insert(PlanDefault)
        }
        plan = localDataSource.getPlanFromType(PlanType.Default)!!
        logger.d(logTag, "getDefaultPlan(): return $plan")
        return plan
    }

    override suspend fun getStandardPlans(): List<Plan> {
        val plans = localDataSource.getStandardPlans()
        logger.d(logTag, "getStandardPlans(): return count(${plans.size})")
        return plans
    }

    override suspend fun getPlanOrDefault(planId: Long): Plan {
        val plan = localDataSource.getPlan(planId) ?: getDefaultPlan()
        logger.d(logTag, "getPlanOrDefault(Long = $planId): return $plan")
        return plan
    }

    override suspend fun getPlan(planId: Long): Plan? {
        val plan = localDataSource.getPlan(planId)
        logger.d(logTag, "getPlan(Long = $planId): return $plan")
        return plan
    }

    override suspend fun insert(plans: List<Plan>) {
        logger.d(logTag, "insert(List<Plan> = count(${plans.size})")
        localDataSource.insert(plans)
    }

    override suspend fun create(title: String): Long {
        val plan = Plan(title = title)
        val result = localDataSource.insert(plan)
        logger.d(logTag, "create(String = $title): return $result")
        return result
    }

    override suspend fun updateTitle(planId: Long, title: String) {
        logger.d(logTag, "updateTitle(Long = $planId, String = $title)")
        localDataSource.updateTitle(planId, title)
    }

    override suspend fun delete(planIds: List<Long>) {
        logger.d(logTag, "delete(List<Long> = count(${planIds.size}))")
        localDataSource.delete(planIds)
    }

    override suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean {
        val result = localDataSource.deletePlanIfNameIsEmpty(planId)
        logger.d(logTag, "deletePlanIfNameIsEmpty(Long = $planId): return $result")
        return result
    }

    override suspend fun isEmpty(): Boolean {
        val result = localDataSource.isEmpty()
        logger.d(logTag, "isEmpty(): return $result")
        return result
    }

    override suspend fun clear() {
        logger.d(logTag, "clear()")
        localDataSource.clear()
    }
}