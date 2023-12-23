package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
	private val localDataSource: PlanLocalDataSource
) : PlanRepository {

	override val standardPlans: Flow<List<Plan>>
		get() = localDataSource.standardPlans

	override suspend fun create(title: String): Plan {
		val plan = Plan(title = title)
		return localDataSource.insert(plan)
	}

	override suspend fun updateTitle(planId: Int, title: String): Plan {
		return localDataSource.updateTitle(planId, title)
	}

	override suspend fun delete(planIds: List<Int>) {
		localDataSource.delete(planIds)
	}

	override suspend fun init() {
		localDataSource.init()
	}

	override suspend fun isEmpty(): Boolean {
		return localDataSource.isEmpty()
	}

	override suspend fun clear() {
		localDataSource.clear()
	}
}