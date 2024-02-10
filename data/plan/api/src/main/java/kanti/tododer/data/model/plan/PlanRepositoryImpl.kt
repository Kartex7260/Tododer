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
			logger.d(logTag, "planAll: Flow<Plan> collect null")
			if (plan == null) {
				logger.d(logTag, "planAll: Flow<Plan> it collect is ignore")
				localDataSource.insert(PlanAll)
			}
		}
		.filterNotNull()

	override val planDefault: Flow<Plan>  = localDataSource.defaultPlan
		.onEach { plan ->
			logger.d(logTag, "planAll: Flow<Plan> collect null")
			if (plan == null) {
				logger.d(logTag, "planAll: Flow<Plan> it collect is ignore")
				localDataSource.insert(PlanDefault)
			}
		}
		.filterNotNull()

	override val standardPlans: Flow<List<Plan>>
		get() = localDataSource.standardPlans

	override suspend fun getPlanOrDefault(planId: Long): Plan {
		return localDataSource.getPlan(planId) ?: getDefaultPlan()
	}

	override suspend fun getStandardPlans(): List<Plan> {
		return localDataSource.getStandardPlans()
	}

	override suspend fun getPlan(planId: Long): Plan? {
		return localDataSource.getPlan(planId)
	}

	override suspend fun getDefaultPlan(): Plan {
		return localDataSource.getPlanFromType(PlanType.Default)
	}

	override suspend fun insert(plans: List<Plan>) {
		localDataSource.insert(plans)
	}

	override suspend fun create(title: String): Long {
		val plan = Plan(title = title)
		return localDataSource.insert(plan)
	}

	override suspend fun updateTitle(planId: Long, title: String) {
		localDataSource.updateTitle(planId, title)
	}

	override suspend fun delete(planIds: List<Long>) {
		logger.d(logTag, "delete(List<Long> = count(${planIds.size}))")
		localDataSource.delete(planIds)
	}

	override suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean {
		return localDataSource.deletePlanIfNameIsEmpty(planId)
	}

	override suspend fun isEmpty(): Boolean {
		return localDataSource.isEmpty()
	}

	override suspend fun clear() {
		localDataSource.clear()
	}
}