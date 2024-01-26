package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.di.PlansQualifier
import kanti.tododer.services.chanceundo.ChanceUndo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
	private val localDataSource: PlanLocalDataSource,
	@PlansQualifier private val chanceUndo: ChanceUndo<List<Plan>>
) : PlanRepository {

	override val planAll: Flow<Plan>
		get() = localDataSource.planAll
	override val planDefault: Flow<Plan>
		get() = localDataSource.defaultPlan
	override val standardPlans: Flow<List<Plan>>
		get() = localDataSource.standardPlans

	private val chanceUndoMutex = Mutex()

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

	override suspend fun delete(planIds: List<Long>): List<Plan> {
		val plans = localDataSource.getPlans(planIds)
		localDataSource.delete(planIds)
		chanceUndoMutex.withLock {
			chanceUndo.register(plans)
		}
		return plans
	}

	override suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean {
		return localDataSource.deletePlanIfNameIsEmpty(planId)
	}

	override suspend fun undoDelete() {
		val plans = chanceUndoMutex.withLock {
			chanceUndo.unregister() ?: return
		}
		localDataSource.insert(plans)
	}

	override suspend fun undoChanceRejected(): List<Plan>? {
		return chanceUndoMutex.withLock {
			chanceUndo.unregister()
		}
	}

	override suspend fun isEmpty(): Boolean {
		return localDataSource.isEmpty()
	}

	override suspend fun clear() {
		localDataSource.clear()
	}
}