package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kotlinx.coroutines.flow.Flow

interface PlanLocalDataSource {

	val standardPlans: Flow<List<Plan>>

	suspend fun insert(plan: Plan): Plan

	suspend fun updateTitle(planId: Int, title: String): Plan

	suspend fun delete(planIds: List<Int>)

	suspend fun init()

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}