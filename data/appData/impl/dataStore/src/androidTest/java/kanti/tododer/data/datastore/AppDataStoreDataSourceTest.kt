package kanti.tododer.data.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kanti.tododer.util.log.AndroidLogger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppDataStoreDataSourceTest {

    private val logTag = "AppDataStoreDataSourceTest"
    private val logger = AndroidLogger()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dataSource = AppDataStoreDataSource(
        appContext = context,
        logger = logger
    )

    @After
    fun after() = runTest {
        logger.d(logTag, "---------- after each ----------")
        dataSource.setCurrentPlan(null)
    }

    @Test
    fun getCurrentPlanIdAndSetCurrentPlanId() = runTest {
        var currentScenery = 1
        val mutex = Mutex()
        val startMutex = Mutex()

        val expected1 = 245L
        val expected2 = -23L

        launch {
            logger.d(logTag, "collecting start")
            startMutex.lock()
            dataSource.currentPlanId.collect {
                mutex.withLock {
                    if (startMutex.isLocked)
                        startMutex.unlock()
                    logger.d(logTag, "collect: $it, current scenery: $currentScenery")
                    when (currentScenery) {
                        1 -> { assertNull(it) }
                        2 -> { assertEquals(expected1, it) }
                        3 -> { assertEquals(expected2, it) }
                        4 -> {
                            assertNull(it)
                            cancel("Success")
                        }
                    }
                }
            }
        }

        launch {
            startMutex.lock()
            mutex.withLock { currentScenery = 2 }
            dataSource.setCurrentPlan(expected1)

            mutex.withLock { currentScenery = 3 }
            dataSource.setCurrentPlan(expected2)

            mutex.withLock { currentScenery = 4 }
            dataSource.setCurrentPlan(null)
        }
    }
}