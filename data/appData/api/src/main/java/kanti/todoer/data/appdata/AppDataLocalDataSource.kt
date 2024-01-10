package kanti.todoer.data.appdata

import kotlinx.coroutines.flow.Flow

interface AppDataLocalDataSource {

	val currentPlanId: Flow<Int>

	suspend fun setCurrentPlan(planId: Int?)
}