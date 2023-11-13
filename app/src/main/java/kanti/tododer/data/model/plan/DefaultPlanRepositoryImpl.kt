package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource

class DefaultPlanRepositoryImpl(
	private val localDataSource: PlanLocalDataSource
) : PlanRepository {

	override suspend fun getPlan(id: Int): RepositoryResult<BasePlan> {
		return localDataSource.getPlan(id).toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<BasePlan>> {
		return localDataSource.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(vararg plan: BasePlan): RepositoryResult<Unit> {
		return localDataSource.insert(*plan).toRepositoryResult()
	}

	override suspend fun insert(plan: BasePlan): RepositoryResult<BasePlan> {
		return localDataSource.insert(plan).toRepositoryResult()
	}

	override suspend fun update(vararg plan: BasePlan): RepositoryResult<Unit> {
		return localDataSource.update(*plan).toRepositoryResult()
	}

	override suspend fun update(
		plan: BasePlan,
		update: (BasePlan.() -> BasePlan)?
	): RepositoryResult<BasePlan> {
		val updatedPlan = update?.let { plan.it() } ?: plan
		return localDataSource.update(updatedPlan).toRepositoryResult()
	}

	override suspend fun delete(vararg plan: BasePlan): RepositoryResult<Unit> {
		return localDataSource.delete(*plan).toRepositoryResult()
	}

	override suspend fun delete(plan: BasePlan): Boolean {
		return localDataSource.delete(plan)
	}

	override suspend fun deleteAll() = localDataSource.deleteAll()

}