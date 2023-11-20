package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult

interface PlanRepository {

	suspend fun getPlan(id: Int): RepositoryResult<Plan>

	suspend fun getChildren(fid: String): RepositoryResult<List<Plan>>

	suspend fun insert(vararg plan: Plan): RepositoryResult<Unit>

	suspend fun insert(plan: Plan): RepositoryResult<Plan>

	suspend fun update(vararg plan: Plan): RepositoryResult<Unit>

	suspend fun update(
		plan: Plan,
		update: (Plan.() -> Plan)? = null
	): RepositoryResult<Plan>

	suspend fun delete(vararg plan: Plan): RepositoryResult<Unit>

	suspend fun delete(plan: Plan): Boolean

	suspend fun deleteAll()

}