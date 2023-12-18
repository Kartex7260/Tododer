package kanti.tododer.data.room.plan

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kanti.tododer.common.logTag
import kanti.tododer.data.room.TododerDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlanDaoTest {

	private val db: TododerDatabase
	private val planDao: PlanDao

	init {
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, TododerDatabase::class.java).build()
		planDao = db.planDao()
	}

	@After
	fun after() = runTest {
		planDao.deleteAll()
	}

	@Test
	fun getArchived() = runTest {
		planDao.insert(PlanEntity(
			id = 1,
			archived = false
		))
		planDao.insert(PlanEntity(
			id = 2,
			archived = true
		))
		planDao.insert(PlanEntity(
			id = 3,
			archived = false
		))
		planDao.insert(PlanEntity(
			id = 4,
			archived = true
		))
		val expected = arrayOf(
			PlanEntity(
				id = 2,
				archived = true
			),
			PlanEntity(
				id = 4,
				archived = true
			)
		)

		val flow = planDao.getAll(true)
		launch {
			flow.collect {
				Log.i(logTag, "Archived flow collect data")
				assertArrayEquals(
					expected,
					it.toTypedArray()
				)
				cancel("Success")
			}
		}
	}

	@Test
	fun getStandard() = runTest {
		planDao.insert(PlanEntity(
			id = 1,
			archived = false
		))
		planDao.insert(PlanEntity(
			id = 2,
			archived = true
		))
		planDao.insert(PlanEntity(
			id = 3,
			archived = false
		))
		planDao.insert(PlanEntity(
			id = 4,
			archived = true
		))
		val expected = arrayOf(
			PlanEntity(
				id = 1,
				archived = false
			),
			PlanEntity(
				id = 3,
				archived = false
			)
		)

		val flow = planDao.getAll(false)
		launch {
			flow.collect {
				Log.i(logTag, "Archived flow collect data")
				assertArrayEquals(
					expected,
					it.toTypedArray()
				)
				cancel("Success")
			}
		}
	}

	@Test
	fun getByRowIdNull() = runTest {
		val plan = planDao.getByRowId(1L)
		assertNull(plan)
	}

	@Test
	fun getByRowId() = runTest {
		planDao.insert(
			PlanEntity(
				id = 1
			)
		)
		val rowId = planDao.insert(
			PlanEntity(
				id = 2
			)
		)

		val plan = planDao.getByRowId(rowId)
		assertEquals(
			PlanEntity(
				id = 2
			),
			plan
		)
	}

	@Test
	fun getPlanNull() = runTest {
		val plan = planDao.getPlan(3)
		assertNull(plan)
	}

	@Test
	fun getPlan() = runTest {
	    planDao.insert(
			PlanEntity(
				id = 1
			)
		)
		planDao.insert(
			PlanEntity(
				id = 2
			)
		)

		val plan = planDao.getPlan(2)
		assertEquals(
			PlanEntity(
				id = 2
			),
			plan
		)
	}

	@Test
	fun count0() = runTest {
		val count = planDao.count()
		val expected = 0
		assertEquals(expected, count)
	}

	@Test
	fun count1() = runTest {
		planDao.insert(PlanEntity())
		planDao.insert(PlanEntity())
		planDao.insert(PlanEntity())
		planDao.insert(PlanEntity())
		val expected = 4

		val count = planDao.count()
		assertEquals(expected, count)
	}

	@Test
	fun deleteAll() = runTest {
		planDao.insert(PlanEntity())
		planDao.insert(PlanEntity())
		planDao.insert(PlanEntity())
		planDao.insert(PlanEntity())
		val expected = 0

		planDao.deleteAll()

		val count = planDao.count()
		assertEquals(expected, count)
	}
}