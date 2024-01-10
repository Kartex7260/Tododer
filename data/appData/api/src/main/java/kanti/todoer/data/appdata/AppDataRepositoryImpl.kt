package kanti.todoer.data.appdata

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
	private val localDataSource: AppDataLocalDataSource
) : AppDataRepository {

	override val currentPlanId: Flow<Int> = localDataSource.currentPlanId

	override suspend fun setCurrentPlan(planId: Int) {
		localDataSource.setCurrentPlan(planId)
	}
}