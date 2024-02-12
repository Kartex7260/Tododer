package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanState
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toPlan
import kanti.tododer.util.log.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlanLocalDataSource(
    private val plans: MutableMap<Long, Plan> = LinkedHashMap(),
    private val logger: Logger
) : PlanLocalDataSource {

    private val logTag = "FakePlanLocalDataSource"

    private val stateFlow: MutableStateFlow<List<Plan>> = MutableStateFlow(plans.values.toList())

    override val planAll: Flow<Plan?> = stateFlow
        .map { plans ->
            val plan = plans.firstOrNull { it.type == PlanType.All }
            logger.d(logTag, "planAll: Flow<Plan?>: collect $plan")
            plan
        }

    override val planDefault: Flow<Plan?> = stateFlow
        .map { plans ->
            val plan = plans.firstOrNull { it.type == PlanType.Default }
            logger.d(logTag, "planAll: Flow<Plan?>: collect $plan")
            plan
        }

    override val standardPlans: Flow<List<Plan>> = stateFlow
        .map { plans ->
            val filteredPlans = plans.filter {
                it.state == PlanState.Normal && it.type == PlanType.Custom
            }
            logger.d(
                logTag,
                "standardPlans: Flow<List<Plan>>: collect plans (${filteredPlans.size})"
            )
            filteredPlans
        }

    override suspend fun getPlan(planId: Long): Plan? {
        val plan = plans.values.firstOrNull { it.id == planId }
        logger.d(logTag, "getPlan(Long = $planId): return $plan")
        return plan
    }

    override suspend fun getPlanFromType(type: PlanType): Plan? {
        if (type == PlanType.Custom)
            throw IllegalArgumentException("getPlanFromType for PlanType. All and Default only")
        val plan = plans.values.firstOrNull { it.type == type }
        logger.d(logTag, "getPlanFromType(PlanType = $type): return $plan")
        return plan
    }

    override suspend fun getPlans(plansId: List<Long>): List<Plan> {
        val plans = plans.values.filter { plansId.contains(it.id) }
        logger.d(
            logTag,
            "getPlans(List<Long> = size(${plansId.size})): return count(${plans.size})"
        )
        return plans
    }

    override suspend fun getStandardPlans(): List<Plan> {
        val plans =
            plans.values.filter { it.state == PlanState.Normal && it.type == PlanType.Custom }
        logger.d(
            logTag,
            "getStandardPlans(): return count(${plans.size})"
        )
        return plans
    }

    override suspend fun insert(plan: Plan): Long {
        val newPlan = if (plan.id == 0L) {
            val lastId = plans.keys.lastOrNull() ?: 0
            plan.toPlan(id = lastId + 1L)
        } else {
            if (plans.containsKey(plan.id))
                return -1L
            plan
        }
        plans[newPlan.id] = newPlan
        logger.d(logTag, "insert(Plan = $plan): return ${newPlan.id}")
        updateStateFlow()
        return newPlan.id
    }

    override suspend fun insert(plans: List<Plan>) {
        logger.d(logTag, "insert(List<Plan> = count(${plans.size}))")
        logger.enabled(false)
        for (plan in plans) {
            insert(plan)
        }
        logger.enabled(true)
    }

    override suspend fun updateTitle(planId: Long, title: String) {
        val plan = requirePlan(planId)
        val newPlan = plan.toPlan(title = title)
        plans[planId] = newPlan
        logger.d(logTag, "updateTitle(Long = $planId, String = $title)")
        updateStateFlow()
    }

    override suspend fun delete(planIds: List<Long>) {
        logger.d(logTag, "delete(List<Long> = count(${planIds.size}))")
        for (planId in planIds) {
            plans.remove(planId)
        }
        updateStateFlow()
    }

    override suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean {
        val result = plans.values.removeIf { it.id == planId && it.title.isEmpty() }
        logger.d(logTag, "deletePlanIfNameIsEmpty(Long = $planId): return $result")
        updateStateFlow()
        return result
    }

    override suspend fun isEmpty(): Boolean {
        val isEmpty = plans.isEmpty()
        logger.d(logTag, "isEmpty(): return $isEmpty")
        return isEmpty
    }

    override suspend fun clear() {
        plans.clear()
        logger.d(logTag, "clear()")
        updateStateFlow()
    }

    suspend fun updateStateFlow() {
        stateFlow.emit(plans.values.toList())
    }

    private fun requirePlan(planId: Long): Plan {
        return plans[planId] ?: throw IllegalArgumentException("Not found plan by id=$planId")
    }
}