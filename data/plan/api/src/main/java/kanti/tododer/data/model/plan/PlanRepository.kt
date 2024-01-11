package kanti.tododer.data.model.plan

import kotlinx.coroutines.flow.Flow

interface PlanRepository {

	val planAll: Flow<Plan>
	val planDefault: Flow<Plan>
	val standardPlans: Flow<List<Plan>>

	suspend fun insert(plans: List<Plan>)

	suspend fun getDefaultPlan(): Plan

	suspend fun getStandardPlans(): List<Plan>

	suspend fun getPlanOrDefault(planId: Int): Plan

	suspend fun getPlan(planId: Int): Plan?

	suspend fun create(title: String): Plan

	suspend fun updateTitle(planId: Int, title: String): Plan

	suspend fun delete(planIds: List<Int>): List<Plan>

	suspend fun undoDelete()

	suspend fun undoChanceRejected(): List<Plan>?

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}