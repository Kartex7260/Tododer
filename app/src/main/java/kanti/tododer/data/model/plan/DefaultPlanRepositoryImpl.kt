package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource

class DefaultPlanRepositoryImpl(
	private val planLocal: PlanLocalDataSource
) : PlanRepository {

	override suspend fun getPlan(id: Int): RepositoryResult<BasePlan> {
		val plan = planLocal.getPlan(id)
		return plan.toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<BasePlan>> = planLocal
		.getChildren(fid).toRepositoryResult()

	override suspend fun insert(plan: BasePlan): RepositoryResult<BasePlan> {
		return planLocal.insert(plan).toRepositoryResult()
	}

	override suspend fun insert(list: List<BasePlan>): RepositoryResult<Unit> {
		return planLocal.insert(list).toRepositoryResult()
	}

	override suspend fun replace(
		plan: BasePlan,
		body: (BasePlan.() -> BasePlan)?
	): RepositoryResult<BasePlan> {
		val newPlan = body?.let { plan.it() }
		return planLocal.replace(newPlan ?: plan).toRepositoryResult()
	}

	override suspend fun replace(list: List<BasePlan>): RepositoryResult<Unit> {
		return planLocal.replace(list).toRepositoryResult()
	}

	override suspend fun delete(plan: BasePlan): Boolean = planLocal.delete(plan)

	override suspend fun delete(list: List<BasePlan>): RepositoryResult<Unit> {
		return planLocal.delete(list).toRepositoryResult()
	}

	override suspend fun deleteAll() = planLocal.deleteAll()

}