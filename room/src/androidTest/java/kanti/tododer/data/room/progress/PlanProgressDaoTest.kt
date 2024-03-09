package kanti.tododer.data.room.progress

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kanti.tododer.data.room.TododerDatabase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlanProgressDaoTest {

    private val db: TododerDatabase
    private val planProgressDao: PlanProgressDao

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TododerDatabase::class.java).build()
        planProgressDao = db.progressDao()
    }

    @Test
    fun getProgressNull() = runTest {
        val planProgress = PlanProgressEntity(planId = 1, progress = 0.5f)
        planProgressDao.insert(planProgress)

        val progress = planProgressDao.getProgress(65)
        assertNull(progress)
    }

    @Test
    fun getProgress() = runTest {
        val expected = 0.5f
        val planProgress = PlanProgressEntity(planId = 1, progress = 0.5f)
        planProgressDao.insert(planProgress)

        val progress = planProgressDao.getProgress(1)
        assertEquals(expected, progress)
    }

    @Test
    fun deleteProgressFail() = runTest {
        val planProgressEntity = PlanProgressEntity(planId = 1, progress = 0.5f)
        planProgressDao.insert(planProgressEntity)

        planProgressDao.deleteProgress(43)

        val result = planProgressDao.getProgress(planProgressEntity.planId)
        assertEquals(planProgressEntity.progress, result)
    }

    @Test
    fun deleteProgress() = runTest {
        val planProgressEntity = PlanProgressEntity(planId = 1, progress = 0.5f)
        planProgressDao.insert(planProgressEntity)

        planProgressDao.deleteProgress(planProgressEntity.planId)

        val result = planProgressDao.getProgress(planProgressEntity.planId)
        assertNull(result)
    }
}
