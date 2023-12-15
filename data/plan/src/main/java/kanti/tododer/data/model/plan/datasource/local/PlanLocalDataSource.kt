package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kotlinx.coroutines.flow.Flow

interface PlanLocalDataSource {

	val standardPlans: Flow<List<Plan>>

	val archivedPlans: Flow<List<Plan>>

	suspend fun insert(plan: Plan): Plan

	suspend fun update(plan: Plan): Plan

	suspend fun update(plans: List<Plan>)

	suspend fun delete(plans: List<Plan>)

	suspend fun init()

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}