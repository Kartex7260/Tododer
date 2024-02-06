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

    private val stateNormal =
        "PlanState:fullClassName=STRING-kanti.tododer.data.model.plan.PlanState.Normal"
    private val typeAll = "All"
    private val typeDefault = "Default"
	private val typeCustom = "Custom"

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
    fun getAllEmpty() = runTest {
        planDao.insert(listOf(
            PlanEntity(1),
            PlanEntity(2),
            PlanEntity(3),
            PlanEntity(4),
            PlanEntity(5),
            PlanEntity(6)
        ))
        val expected = arrayOf<PlanEntity>()

        val plans = planDao.getAll(listOf(8, 9, 12))

        assertArrayEquals(expected, plans.toTypedArray())
    }

    @Test
    fun getAll() = runTest {
        planDao.insert(listOf(
            PlanEntity(1),
            PlanEntity(2),
            PlanEntity(3),
            PlanEntity(4),
            PlanEntity(5),
            PlanEntity(6)
        ))
        val expected = arrayOf(
            PlanEntity(2),
            PlanEntity(4),
            PlanEntity(5)
        )

        val plans = planDao.getAll(listOf(2, 4, 5))

        assertArrayEquals(expected, plans.toTypedArray())
    }

    @Test
    fun getAllPlansFlowEmptyState() = runTest {
        planDao.insert(listOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        ))
        val expected = arrayOf<PlanEntity>()

        val flow = planDao.getAllPlansFlow("Foo-foo", "Custom")
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
    fun getAllPlansFlowEmptyType() = runTest {
        planDao.insert(listOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        ))
        val expected = arrayOf<PlanEntity>()

        val flow = planDao.getAllPlansFlow("PlanState.Normal", "All")
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
    fun getAllPlansFlow() = runTest {
        planDao.insert(listOf(
			PlanEntity(
				id = 1,
				state = stateNormal,
                type = typeCustom
			),
			PlanEntity(
				id = 2,
				state = stateNormal,
                type = typeCustom
			),
			PlanEntity(
				id = 3,
				state = stateNormal,
                type = typeCustom
			),
			PlanEntity(
				id = 4,
				state = stateNormal,
                type = typeCustom
			)
		))
        val expected = arrayOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        )

        val flow = planDao.getAllPlansFlow("PlanState.Normal", "Custom")
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
    fun getAllPlansEmptyState() = runTest {
        planDao.insert(listOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        ))
        val expected = arrayOf<PlanEntity>()

        val plans = planDao.getAllPlans("Foo-foo", "Custom")
        assertArrayEquals(
            expected,
            plans.toTypedArray()
        )
    }

    @Test
    fun getAllPlansEmptyType() = runTest {
        planDao.insert(listOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        ))
        val expected = arrayOf<PlanEntity>()

        val plans = planDao.getAllPlans("PlanState.Normal", "All")
        assertArrayEquals(
            expected,
            plans.toTypedArray()
        )
    }

    @Test
    fun getAllPlans() = runTest {
        planDao.insert(listOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        ))
        val expected = arrayOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 3,
                state = stateNormal,
                type = typeCustom
            ),
            PlanEntity(
                id = 4,
                state = stateNormal,
                type = typeCustom
            )
        )

        val plans = planDao.getAllPlans("PlanState.Normal", "Custom")
        assertArrayEquals(
            expected,
            plans.toTypedArray()
        )
    }

    @Test
    fun getFromTypeFlowNull() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(-2, type = typeAll),
                PlanEntity(-1, type = typeDefault),
                PlanEntity(1, type = typeCustom),
                PlanEntity(2, type = typeCustom)
            )
        )

        val actualFlow = planDao.getFromTypeFlow("Foo")
        launch {
            actualFlow.collect {
                assertNull(it)
                cancel("Success")
            }
        }
    }

    @Test
    fun getFromTypeFlowAll() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(-2, type = typeAll),
                PlanEntity(-1, type = typeDefault),
                PlanEntity(1, type = typeCustom),
                PlanEntity(2, type = typeCustom)
            )
        )
        val expected = PlanEntity(-2, type = typeAll)

        val actualFlow = planDao.getFromTypeFlow("All")
        launch {
            actualFlow.collect {
                assertEquals(expected, it)
                cancel("Success")
            }
        }
    }

    @Test
    fun getFromTypeFlowDefault() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(-2, type = typeAll),
                PlanEntity(-1, type = typeDefault),
                PlanEntity(1, type = typeCustom),
                PlanEntity(2, type = typeCustom)
            )
        )
        val expected = PlanEntity(-1, type = typeDefault)

        val actualFlow = planDao.getFromTypeFlow("Default")
        launch {
            actualFlow.collect {
                assertEquals(expected, it)
                cancel("Success")
            }
        }
    }

    @Test
    fun getFromTypeNull() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(-2, type = typeAll),
                PlanEntity(-1, type = typeDefault),
                PlanEntity(1, type = typeCustom),
                PlanEntity(2, type = typeCustom)
            )
        )

        val actual = planDao.getFromType("Foo")
        assertNull(actual)
    }

    @Test
    fun getFromTypeAll() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(-2, type = typeAll),
                PlanEntity(-1, type = typeDefault),
                PlanEntity(1, type = typeCustom),
                PlanEntity(2, type = typeCustom)
            )
        )
        val expected = PlanEntity(-2, type = typeAll)

        val actual = planDao.getFromType("All")
        assertEquals(expected, actual)
    }

    @Test
    fun getFromTypeDefault() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(-2, type = typeAll),
                PlanEntity(-1, type = typeDefault),
                PlanEntity(1, type = typeCustom),
                PlanEntity(2, type = typeCustom)
            )
        )
        val expected = PlanEntity(-1, type = typeDefault)

        val actual = planDao.getFromType("Default")
        assertEquals(expected, actual)
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
    fun updateTitle1() = runTest {
        val expectedArray = arrayOf(
            PlanEntity(id = 1, state = stateNormal, title = "Test 1")
        )

        planDao.insert(PlanEntity(id = 1, state = stateNormal, title = "Test 1"))
        planDao.updateTitle(4, "Updated")

        assertArrayEquals(expectedArray, planDao.getAll().toTypedArray())
    }

    @Test
    fun updateTitle2() = runTest {
        val expectedArray = arrayOf(
            PlanEntity(id = 1, state = stateNormal, title = "Updated")
        )

        planDao.insert(PlanEntity(id = 1, state = stateNormal, title = "Test 1"))
        planDao.updateTitle(1, "Updated")

        assertArrayEquals(expectedArray, planDao.getAll().toTypedArray())
    }

    @Test
    fun delete0() = runTest {
        planDao.insert(listOf(
            PlanEntity(1),
            PlanEntity(2),
            PlanEntity(3),
            PlanEntity(4),
            PlanEntity(5),
            PlanEntity(6)
        ))
        val expected = arrayOf(
            PlanEntity(1),
            PlanEntity(2),
            PlanEntity(3),
            PlanEntity(4),
            PlanEntity(5),
            PlanEntity(6)
        )

        planDao.delete(listOf(9, 120))
        val actual = planDao.getAll()
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    fun delete1() = runTest {
        planDao.insert(
            listOf(
                PlanEntity(1),
                PlanEntity(2),
                PlanEntity(3),
                PlanEntity(4),
                PlanEntity(5),
                PlanEntity(6)
            )
        )
        val expected = arrayOf(
            PlanEntity(1),
            PlanEntity(3),
            PlanEntity(4),
            PlanEntity(5)
        )

        planDao.delete(listOf(2, 6))
        val actual = planDao.getAll()
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    fun deleteIfNameEmptyFail() = runTest {
        planDao.insert(listOf(
            PlanEntity(1, "Test"),
            PlanEntity(2, "")
        ))
        val expected = 0

        val actual = planDao.deleteIfNameEmpty(1)
        assertEquals(expected, actual)
    }

    @Test
    fun deleteIfNameEmptySuccess() = runTest {
        planDao.insert(listOf(
            PlanEntity(1, "Test"),
            PlanEntity(2, "")
        ))
        val expected = 1

        val actual = planDao.deleteIfNameEmpty(2)
        assertEquals(expected, actual)
    }

    @Test
    fun count0() = runTest {
        val count = planDao.count()
        val expected = 0L
        assertEquals(expected, count)
    }

    @Test
    fun count1() = runTest {
        planDao.insert(PlanEntity())
        planDao.insert(PlanEntity())
        planDao.insert(PlanEntity())
        planDao.insert(PlanEntity())
        val expected = 4L

        val count = planDao.count()
        assertEquals(expected, count)
    }

    @Test
    fun deleteAll() = runTest {
        planDao.insert(PlanEntity())
        planDao.insert(PlanEntity())
        planDao.insert(PlanEntity())
        planDao.insert(PlanEntity())
        val expected = 0L

        planDao.deleteAll()

        val count = planDao.count()
        assertEquals(expected, count)
    }
}