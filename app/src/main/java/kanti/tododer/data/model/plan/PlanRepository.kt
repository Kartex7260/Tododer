package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult

interface PlanRepository {

	suspend fun getPlan(id: Int): RepositoryResult<BasePlan>

	suspend fun getChildren(fid: String): RepositoryResult<List<BasePlan>>

	suspend fun insert(vararg plan: BasePlan): RepositoryResult<Unit>

	suspend fun insert(plan: BasePlan): RepositoryResult<BasePlan>

	suspend fun update(vararg plan: BasePlan): RepositoryResult<Unit>

	suspend fun update(
		plan: BasePlan,
		update: (BasePlan.() -> BasePlan)? = null
	): RepositoryResult<BasePlan>

	suspend fun delete(vararg plan: BasePlan): RepositoryResult<Unit>

	suspend fun delete(plan: BasePlan): Boolean

	suspend fun deleteAll()

}