package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult

interface PlanRepository {

	suspend fun getPlan(id: Int): RepositoryResult<BasePlan>

	suspend fun getChildren(fid: String): RepositoryResult<List<BasePlan>>

	suspend fun insert(plan: BasePlan): RepositoryResult<BasePlan>

	suspend fun replace(
		plan: BasePlan,
		body: (BasePlan.() -> BasePlan)? = null
	): RepositoryResult<BasePlan>

	suspend fun delete(plan: BasePlan): Boolean

	suspend fun deleteAll()

}