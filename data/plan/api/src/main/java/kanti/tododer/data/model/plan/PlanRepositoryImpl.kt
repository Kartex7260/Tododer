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

	override suspend fun updateTitle(plan: Plan, title: String): Plan {
		return localDataSource.update(plan.toPlan(
			title = title
		))
	}

	override suspend fun delete(plans: List<Plan>) {
		localDataSource.delete(plans)
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