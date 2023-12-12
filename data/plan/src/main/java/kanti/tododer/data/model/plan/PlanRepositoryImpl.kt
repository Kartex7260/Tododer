package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
	private val planLocal: PlanLocalDataSource
) : PlanRepository {

	override suspend fun getPlan(id: Int): GetRepositoryResult<Plan> {
		return planLocal.getPlan(id).asRepositoryResult
	}

	override suspend fun getChildren(fid: String): Result<List<Plan>> {
		return planLocal.getChildren(fid)
	}

	override suspend fun insert(plan: Plan): Result<Plan> {
		return planLocal.insert(plan)
	}

	override suspend fun insert(vararg plan: Plan): Result<Unit> {
		return planLocal.insert(*plan)
	}

	override suspend fun delete(vararg plan: Plan): Result<Unit> {
		return planLocal.delete(*plan)
	}

	override suspend fun deleteAll(): Result<Unit> {
		return planLocal.deleteAll()
	}

}