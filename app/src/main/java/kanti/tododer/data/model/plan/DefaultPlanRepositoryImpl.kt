package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource

class DefaultPlanRepositoryImpl(
	private val localDataSource: PlanLocalDataSource
) : PlanRepository {

	override suspend fun getPlan(id: Int): RepositoryResult<Plan> {
		return localDataSource.getPlan(id).toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<Plan>> {
		return localDataSource.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(vararg plan: Plan): RepositoryResult<Unit> {
		return localDataSource.insert(*plan).toRepositoryResult()
	}

	override suspend fun insert(plan: Plan): RepositoryResult<Plan> {
		return localDataSource.insert(plan).toRepositoryResult()
	}

	override suspend fun update(vararg plan: Plan): RepositoryResult<Unit> {
		return localDataSource.update(*plan).toRepositoryResult()
	}

	override suspend fun update(
		plan: Plan,
		update: (Plan.() -> Plan)?
	): RepositoryResult<Plan> {
		val updatedPlan = update?.let { plan.it() } ?: plan
		return localDataSource.update(updatedPlan).toRepositoryResult()
	}

	override suspend fun delete(vararg plan: Plan): RepositoryResult<Unit> {
		return localDataSource.delete(*plan).toRepositoryResult()
	}

	override suspend fun delete(plan: Plan): Boolean {
		return localDataSource.delete(plan)
	}

	override suspend fun deleteAll() = localDataSource.deleteAll()

}