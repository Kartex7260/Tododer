package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult

interface PlanRepository {

	suspend fun getPlan(id: Int): RepositoryResult<Plan>

	suspend fun getChildren(fid: String): RepositoryResult<List<Plan>>

	suspend fun insert(plan: Plan): RepositoryResult<Plan>

	suspend fun replace(plan: Plan, body: (Plan.() -> Plan)? = null): RepositoryResult<Plan>

	suspend fun delete(plan: Plan): Boolean

	suspend fun deleteAll()

}