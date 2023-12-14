package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.model.plan.Plan
import kotlinx.coroutines.flow.Flow

interface PlanLocalDataSource {

	val standardPlans: Flow<List<Plan>>

	val archivedPlans: Flow<List<Plan>>

	suspend fun insert(vararg plan: Plan)

	suspend fun update(vararg plan: Plan)

	suspend fun delete(vararg plan: Plan)

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}