package kanti.todoer.data.appdata

import kotlinx.coroutines.flow.Flow

interface AppDataRepository {

	val currentPlanId: Flow<Long>

	suspend fun setCurrentPlan(planId: Long)

	suspend fun deleteCurrentPlan()

	suspend fun deleteIfCurrent(planId: Long)
}