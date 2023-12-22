package kanti.tododer.data.model.plan

import kotlinx.coroutines.flow.Flow

interface PlanRepository {

	val standardPlans: Flow<List<Plan>>

	suspend fun create(title: String): Plan

	suspend fun updateTitle(plan: Plan, title: String): Plan

	suspend fun delete(plans: List<Plan>)

	suspend fun init()

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}