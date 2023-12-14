package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.result.GetRepositoryResult

interface PlanRepository {

	val standardPlans: Flow<List<Plan>>

	val archivedPlans: Flow<List<Plan>>

	suspend fun insert(plan: Plan): Result<Plan>

	suspend fun insert(vararg plan: Plan): Result<Unit>

	suspend fun delete(vararg plan: Plan)

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}