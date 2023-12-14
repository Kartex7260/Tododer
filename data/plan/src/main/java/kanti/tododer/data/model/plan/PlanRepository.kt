package kanti.tododer.data.model.plan

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kotlinx.coroutines.flow.Flow

interface PlanRepository {

	val standardPlans: Flow<List<Plan>>

	val archivedPlans: Flow<List<Plan>>

	suspend fun insert(vararg plan: Plan)

	suspend fun update(vararg plan: Plan)

	suspend fun delete(vararg plan: Plan)

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}