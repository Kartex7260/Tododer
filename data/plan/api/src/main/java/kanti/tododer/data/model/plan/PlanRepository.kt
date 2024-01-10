package kanti.tododer.data.model.plan

import kotlinx.coroutines.flow.Flow

interface PlanRepository {

	val planAll: Flow<Plan>
	val planDefault: Flow<Plan>
	val standardPlans: Flow<List<Plan>>

	suspend fun create(title: String): Plan

	suspend fun updateTitle(planId: Int, title: String): Plan

	suspend fun delete(planIds: List<Int>): List<Plan>

	suspend fun undoDelete()

	suspend fun undoChanceRejected()

	suspend fun init()

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}