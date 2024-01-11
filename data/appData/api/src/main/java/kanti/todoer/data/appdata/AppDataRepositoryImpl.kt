package kanti.todoer.data.appdata

import kanti.tododer.common.Const
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
	private val localDataSource: AppDataLocalDataSource
) : AppDataRepository {

	private var _currentPlanId: Int? = null
	private val mutex = Mutex()

	override val currentPlanId: Flow<Int> = localDataSource.currentPlanId
		.onEach {
			if (it == null) {
				setCurrentPlan(Const.PlansIds.DEFAULT)
			}
			mutex.withLock {
				_currentPlanId = it
			}
		}
		.filterNotNull()

	override suspend fun setCurrentPlan(planId: Int) {
		localDataSource.setCurrentPlan(planId)
	}

	override suspend fun deleteCurrentPlan() {
		localDataSource.setCurrentPlan(null)
	}

	override suspend fun deleteIfCurrent(planId: Int) {
		return mutex.withLock {
			_currentPlanId?.also {
				if (it == planId) {
					deleteCurrentPlan()
				}
			}
		}
	}
}