package kanti.todoer.data.appdata

import kotlinx.coroutines.flow.Flow

interface AppDataLocalDataSource {

	val currentPlanId: Flow<Long?>

	suspend fun setCurrentPlan(planId: Long?)
}