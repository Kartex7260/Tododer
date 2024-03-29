package kanti.todoer.data.appdata

import kanti.tododer.common.Const
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
	private val localDataSource: AppDataLocalDataSource,
	@StandardLog private val logger: Logger
) : AppDataRepository {

	private val logTag = "AppDataRepositoryImpl"

	private var _currentId: Long? = null
	private val mutex = Mutex()

	override val currentPlanId: Flow<Long> = localDataSource.currentPlanId
		.onEach {
			logger.d(logTag, "currentPlanId: Flow<Long> onEach { $it }")
			if (it == null) {
				logger.d(logTag, "currentPlanId is null, set default plan id and filter")
				mutex.lock()
				setCurrentPlan(Const.PlansIds.DEFAULT)
			} else {
				logger.d(logTag, "_currentId set $it")
				_currentId = it
				if (mutex.isLocked)
					mutex.unlock()
			}
		}
		.filterNotNull()

	override suspend fun setCurrentPlan(planId: Long) {
		logger.d(logTag, "setCurrentPlan(Long = $planId)")
		localDataSource.setCurrentPlan(planId)
	}

	override suspend fun deleteCurrentPlan() {
		logger.d(logTag, "deleteCurrentPlan()")
		localDataSource.setCurrentPlan(null)
	}

	override suspend fun deleteIfCurrent(planId: Long) {
		logger.d(logTag, "deleteIfCurrent(Long = $planId)")
		mutex.withLock {
			when (_currentId) {
				planId -> {
					logger.d(logTag, "deleteIfCurrent(Long = $planId): plan is current")
					deleteCurrentPlan()
				}
				null -> {
					logger.d(logTag, "deleteIfCurrent(Long = $planId): " +
							"_currentPlanId is null, don`t invoke deleteCurrentPlan()")
				}
				else -> {
					logger.d(logTag, "deleteIfCurrent(Long = $planId): plan is don`t current")
				}
			}
		}
	}
}