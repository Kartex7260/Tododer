package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.result.GetRepositoryResult

interface PlanRepository {

	suspend fun getPlan(id: Int): GetRepositoryResult<Plan>

	suspend fun getChildren(fid: String): Result<List<Plan>>

	suspend fun insert(plan: Plan): Result<Plan>

	suspend fun insert(vararg plan: Plan): Result<Unit>

	suspend fun delete(vararg plan: Plan): Result<Unit>

	suspend fun deleteAll(): Result<Unit>

}