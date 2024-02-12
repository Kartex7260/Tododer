package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.plan.PlanEntity
import kanti.tododer.util.log.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlanDao(
    private val plans: MutableMap<Long, PlanEntity> = LinkedHashMap(),
    private val logger: Logger
) : PlanDao {

    private val logTag = "FakePlanDao"

    private val plansFlow: MutableStateFlow<List<PlanEntity>> =
        MutableStateFlow(plans.values.toList())

    suspend fun updateFlow() {
        logger.d(logTag, "updateFlow()")
        plansFlow.emit(plans.values.toList())
    }

    override suspend fun getAll(): List<PlanEntity> {
        val result = plans.values.toList()
        logger.d(logTag, "getAll(): return count(${result.size})")
        return result
    }

    override suspend fun getAll(plansId: List<Long>): List<PlanEntity> {
        val result = plans.values.filter { plansId.contains(it.id) }
        logger.d(
            logTag,
            "getAll(List<Long> = count(${plansId.size})): return count(${result.size})"
        )
        return result
    }

    override fun getAllPlansFlow(state: String, type: String): Flow<List<PlanEntity>> {
        val result = plansFlow.map { plans ->
            val result = plans.filter {
                it.state.contains(state) && it.type.contains(type)
            }
            logger.d(
                logTag, "plansFlow: Flow<List<PlanEntity>>: (state = $state, type = $type)" +
                        "  collect(${plans.size}), filter(${plans.size - result.size}), emit(${result.size})"
            )
            result
        }
        logger.d(logTag, "getAllPlansFlow(String = $state, String = $type): return $result")
        return result
    }

    override suspend fun getAllPlans(state: String, type: String): List<PlanEntity> {
        val result = plans.values.filter {
            it.state.contains(state) && it.type == type
        }
        logger.d(logTag, "getAllPlans(String = $state, String = $type): return ${result.size}")
        return result
    }

    override fun getFromTypeFlow(type: String): Flow<PlanEntity?> {
        val result = plansFlow.map { plans ->
            val result = plans.firstOrNull { it.type == type }
            logger.d(
                logTag, "plansFromType: Flow<PlanEntity?>: (type=$type) collect(${plans.size}), " +
                        "filter(${plans.size - if (result == null) 0 else 1}), emit($result)"
            )
            result
        }
        logger.d(logTag, "getFromTypeFlow(String = $type): return $result")
        return result
    }

    override suspend fun getFromType(type: String): PlanEntity? {
        val result = plans.values.firstOrNull { it.type == type }
        logger.d(logTag, "getFromType(String = $type): return $result")
        return result
    }

    override suspend fun getByRowId(rowId: Long): PlanEntity? {
        val result = plans[rowId]
        logger.d(logTag, "getByRowId(Long = $rowId): return $result")
        return result
    }

    override suspend fun getPlan(id: Long): PlanEntity? {
        val result = plans[id]
        logger.d(logTag, "getByRowId(Long = $id): return $result")
        return result
    }

    override suspend fun insert(plan: PlanEntity): Long {
        val newPlan = if (plan.id == 0L) {
            val lastId = plans.keys.lastOrNull() ?: 0
            plan.copy(id = lastId + 1)
        } else {
            if (plans.containsKey(plan.id))
                return -1L
            plan
        }
        plans[newPlan.id] = newPlan
        logger.d(logTag, "insert(PlanEntity = $plan): return ${newPlan.id}")
        updateFlow()
        return newPlan.id
    }

    override suspend fun insert(plans: List<PlanEntity>) {
        logger.d(logTag, "insert(List<PlanEntity> = count(${plans.size}))")
        logger.enabled(false)
        for (plan in plans) {
            insert(plan)
        }
        logger.enabled(true)
    }

    override suspend fun updateTitle(planId: Long, title: String) {
        val plan = plans[planId] ?: return
        val newPlan = plan.copy(title = title)
        plans[planId] = newPlan
        logger.d(logTag, "updateTitle(Long = $planId, String = $title)")
        updateFlow()
    }

    override suspend fun delete(planIds: List<Long>) {
        for (planId in planIds) {
            plans.remove(planId)
        }
        logger.d(logTag, "delete(List<Long> = count(${planIds.size}))")
        updateFlow()
    }

    override suspend fun deleteIfNameEmpty(planId: Long): Int {
        val plan = plans[planId] ?: return 0
        val res = if (plan.title.isEmpty()) plans.remove(planId) else null
        val result = if (res == null) 0 else 1
        logger.d(logTag, "deleteIfNameEmpty(Long = $planId): return $result")
        updateFlow()
        return result
    }

    override suspend fun count(): Long {
        val result = plans.size.toLong()
        logger.d(logTag, "count(): return $result")
        return result
    }

    override suspend fun deleteAll() {
        plans.clear()
        logger.d(logTag, "deleteAll()")
        updateFlow()
    }
}