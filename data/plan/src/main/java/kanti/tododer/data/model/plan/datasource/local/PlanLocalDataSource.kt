package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.model.plan.Plan

interface PlanLocalDataSource {

	val standardPlans: Flow<List<Plan>>

	val archivedPlans: Flow<List<Plan>>

	suspend fun insert(plan: Plan): Result<Plan>

	suspend fun insert(vararg plan: Plan): Result<Unit>

	suspend fun delete(vararg plan: Plan)

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}