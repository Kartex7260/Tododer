package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kotlinx.coroutines.flow.Flow

interface PlanLocalDataSource {

	val planAll: Flow<Plan?>
	val defaultPlan: Flow<Plan?>
	val standardPlans: Flow<List<Plan>>

	suspend fun getPlan(planId: Long): Plan?

	suspend fun getPlanFromType(type: PlanType): Plan

	suspend fun getPlans(plansId: List<Long>): List<Plan>

	suspend fun getStandardPlans(): List<Plan>

	suspend fun insert(plan: Plan): Long

	suspend fun insert(plans: List<Plan>)

	suspend fun updateTitle(planId: Long, title: String)

	suspend fun delete(planIds: List<Long>)

	suspend fun deletePlanIfNameIsEmpty(planId: Long): Boolean

	suspend fun isEmpty(): Boolean

	suspend fun clear()

}