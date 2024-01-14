package kanti.tododer.data.model.plan

import kotlinx.coroutines.flow.Flow

interface PlanRepository {

	val planAll: Flow<Plan>
	val planDefault: Flow<Plan>
	val standardPlans: Flow<List<Plan>>

	suspend fun insert(plans: List<Plan>)

	suspend fun getDefaultPlan(): Plan

	suspend fun getStandardPlans(): List<Plan>

	suspend fun getPlanOrDefault(planId: Long): Plan

	suspend fun getPlan(planId: Long): Plan?

	suspend fun create(title: String): Long

	suspend fun updateTitle(planId: Long, title: String)

	suspend fun delete(planIds: List<Long>): List<Plan>

	suspend fun undoDelete()

	suspend fun undoChanceRejected(): List<Plan>?

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}