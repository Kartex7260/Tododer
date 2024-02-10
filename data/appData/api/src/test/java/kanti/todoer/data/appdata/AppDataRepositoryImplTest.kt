package kanti.todoer.data.appdata

import kanti.tododer.common.Const
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppDataRepositoryImplTest {

    private val logger = PrintLogger()
    private val logTag = "AppDataRepositoryImplTest"

    private val dataSource = FakeAppDataLocalDataSource()
    private val appDataRepository = AppDataRepositoryImpl(
        localDataSource = dataSource,
        logger = logger
    )

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "------- AFTER EACH -------")
        dataSource.clear()
    }

    @Test
    fun getCurrentPlanId() = runTest {
        var currentScenery = 1
        val mutex = Mutex()

        launch {
            logger.d(logTag, "collecting: start")
            appDataRepository.currentPlanId
                .collect {
                    logger.d(logTag, "collecting: collect from repository $it")

                    mutex.lock()
                    when (currentScenery) {
                        1 -> {
                            assertEquals(-1L, it)
                        }
                        2 -> {
                            assertEquals(200L, it)
                        }
                        3 -> {
                            assertEquals(-90135161L, it)
                        }
                        4 -> {
                            assertEquals(Const.PlansIds.DEFAULT, it)
                        }
                        5 -> {
                            cancel("Success")
                        }
                    }
                    mutex.unlock()
                }
        }

        launch {
            mutex.withLock { currentScenery = 2 }
            appDataRepository.setCurrentPlan(200L)

            mutex.withLock { currentScenery = 3 }
            appDataRepository.setCurrentPlan(-90135161L)

            mutex.withLock { currentScenery = 4 }
            appDataRepository.deleteCurrentPlan()

            mutex.withLock { currentScenery = 5 }
        }
    }

    @Test
    fun deleteIfCurrentFail() = runTest {
        val expected = 1L
        val initialPlanId = 1L

        launch {
            logger.d(logTag, "collecting: start")
            var currentCycle = 0
            val maxCycles = 1
            val actual = appDataRepository.currentPlanId
                .onEach {
                    logger.d(logTag, "collecting: collect from repository $it")
                    if (currentCycle < maxCycles) {
                        logger.d(logTag, "collecting: is collect dropped")
                        currentCycle++
                    }
                }
                .drop(maxCycles)
                .first()
            logger.d(logTag, "collecting: get $actual")
            assertEquals(expected, actual)
        }

        launch {
            logger.d(logTag, "appDataRepository.setCurrentPlan($initialPlanId)")
            appDataRepository.setCurrentPlan(initialPlanId)
            logger.d(logTag, "appDataRepository.deleteIfCurrent(1)")
            appDataRepository.deleteIfCurrent(246L)
        }
    }

    @Test
    fun deleteIfCurrentSuc() = runTest {
        val expected = -1L
        val initialPlanId = 1L

        launch {
            logger.d(logTag, "collecting: start")
            var currentCycle = 0
            val maxCycles = 2
            val actual = appDataRepository.currentPlanId
                .onEach {
                    logger.d(logTag, "collecting: collect from repository $it")
                    if (currentCycle < maxCycles) {
                        logger.d(logTag, "collecting: is collect dropped")
                        currentCycle++
                    }
                }
                .drop(maxCycles)
                .first()
            logger.d(logTag, "collecting: get $actual")
            assertEquals(expected, actual)
        }

        launch {
            logger.d(logTag, "appDataRepository.setCurrentPlan($initialPlanId)")
            appDataRepository.setCurrentPlan(initialPlanId)
            logger.d(logTag, "appDataRepository.deleteIfCurrent(1)")
            appDataRepository.deleteIfCurrent(1L)
        }
    }
}