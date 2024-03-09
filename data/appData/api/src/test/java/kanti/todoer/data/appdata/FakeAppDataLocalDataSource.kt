package kanti.todoer.data.appdata

import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach

class FakeAppDataLocalDataSource(
    private val logger: Logger = PrintLogger()
) : AppDataLocalDataSource {

    private val logTag = "FakeAppDataLocalDataSource"

    private val _currentPlanId = MutableStateFlow<Long?>(null)
    override val currentPlanId: Flow<Long?> = _currentPlanId
        .onEach {
            logger.d(logTag, "currentPlanId: Flow<Long?> onEach { $it }")
        }

    override suspend fun setCurrentPlan(planId: Long?) {
        logger.d(logTag, "setCurrentPlan(Long? = $planId)")
        _currentPlanId.emit(planId)
    }

    suspend fun clear() {
        _currentPlanId.emit(null)
    }
}