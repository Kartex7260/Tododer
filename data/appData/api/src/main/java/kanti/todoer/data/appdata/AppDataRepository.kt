package kanti.todoer.data.appdata

import kotlinx.coroutines.flow.Flow

interface AppDataRepository {

	val currentPlanId: Flow<Int>

	suspend fun setCurrentPlan(planId: Int)
}