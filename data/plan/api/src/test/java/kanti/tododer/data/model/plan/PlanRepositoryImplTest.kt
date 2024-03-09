package kanti.tododer.data.model.plan

import kanti.tododer.data.model.plan.datasource.local.FakePlanLocalDataSource
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PlanRepositoryImplTest {

    private val logTag = "PlanRepositoryImplTest"
    private val logger = PrintLogger()

    private val plansMap: MutableMap<Long, Plan> = LinkedHashMap()
    private val planDataSource = FakePlanLocalDataSource(
        plans = plansMap,
        logger = logger
    )
    private val repository: PlanRepository = PlanRepositoryImpl(
        localDataSource = planDataSource,
        logger = logger
    )

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "----------- after each -----------")
        plansMap.clear()
    }

    @Test
    @DisplayName("planAll: Flow<Plan>")
    fun planAll() = runTest {
        val plan = repository.planAll.first()
        assertEquals(PlanAll, plan)
    }

    @Test
    @DisplayName("planDefault: Flow<Plan>")
    fun planDefault() = runTest {
        val plan = repository.planDefault.first()
        assertEquals(PlanDefault, plan)
    }

    @Test
    @DisplayName("standardPlans: Flow<List<Plan>>")
    fun standardPlans() = runTest(StandardTestDispatcher()) {
        launch {
            repository.standardPlans.collectIndexed { index, value ->
                when (index) {
                    0 -> { assertArrayEquals(arrayOf(), value.toTypedArray()) }
                    1 -> { assertArrayEquals(arrayOf(Plan(id = 1L)), value.toTypedArray()) }
                    2 -> {
                        assertArrayEquals(
                            arrayOf(
                                Plan(id = 1L),
                                Plan(id = 2L)
                            ),
                            value.toTypedArray()
                        )
                        cancel("Success")
                    }
                }
            }
        }

        launch {
            plansMap[1L] = Plan(id = 1L)
            planDataSource.updateStateFlow()

            delay(500L)

            plansMap[2L] = Plan(id = 2L)
            planDataSource.updateStateFlow()
        }
    }

    @Test
    @DisplayName("getDefaultPlan()")
    fun getDefaultPlan() = runTest {
        val plan = repository.getDefaultPlan()
        assertEquals(PlanDefault, plan)
    }

    @Test
    @DisplayName("getStandardPlans()")
    fun getStandardPlans() = runTest {
        var expected = arrayOf<Plan>()
        var plans = repository.getStandardPlans()
        assertArrayEquals(expected, plans.toTypedArray())

        plansMap.putAll(
            listOf(
                1L to Plan(id = 1L),
                2L to Plan(id = 2L),
                3L to Plan(id = 3L)
            )
        )
        expected = arrayOf(Plan(id = 1L), Plan(id = 2L), Plan(id = 3L))

        plans = repository.getStandardPlans()
        assertArrayEquals(expected, plans.toTypedArray())
    }

    @Test
    @DisplayName("getPlanOrDefault(Long)")
    fun getPlanOrDefault() = runTest {
        val planId = 5L
        var expected = PlanDefault

        var actual = repository.getPlanOrDefault(planId)
        assertEquals(expected, actual)

        expected = Plan(id = planId)
        plansMap[planId] = expected

        actual = repository.getPlanOrDefault(planId)
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("getPlan(Long)")
    fun getPlan() = runTest {
        val planId = 5L

        var actual = repository.getPlan(planId)
        assertNull(actual)

        val expected = Plan(id = planId)
        plansMap[planId] = expected

        actual = repository.getPlan(planId)
        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("insert(List<Plan>)")
    fun insert() = runTest {
        var expected = arrayOf(
            Plan(id = 1L),
            Plan(id = 2L),
            Plan(id = 3L)
        )
        repository.insert(
            listOf(
                Plan(id = 1L),
                Plan(id = 2L),
                Plan(id = 3L)
            )
        )

        assertArrayEquals(expected, plansMap.values.toTypedArray())

        expected = arrayOf(
            Plan(id = 1L),
            Plan(id = 2L),
            Plan(id = 3L),
            Plan(id = 5L)
        )
        repository.insert(
            listOf(
                Plan(id = 2L),
                Plan(id = 3L),
                Plan(id = 5L)
            )
        )
        assertArrayEquals(expected, plansMap.values.toTypedArray())
    }

    @Test
    @DisplayName("create(String)")
    fun create() = runTest {
        val expectedPlan = Plan(id = 1, title = "Test")
        val planId = repository.create("Test")

        val expectedArray = arrayOf(
            Plan(id = 1, title = "Test")
        )

        assertEquals(expectedPlan.id, planId)
        assertArrayEquals(expectedArray, plansMap.values.toTypedArray())
    }

    @Test
    @DisplayName("updateTitle(Plan, String)")
    fun updateTitle() = runTest {
        val expectedArray = arrayOf(Plan(id = 1, title = "Updated"))
        plansMap.putAll(
            mapOf(
                1L to Plan(id = 1, title = "Test 1")
            )
        )

        repository.updateTitle(1, title = "Updated")

        assertArrayEquals(expectedArray, plansMap.values.toTypedArray())
    }

    @Test
    @DisplayName("delete(List<Plan>)")
    fun delete() = runTest {
        plansMap.putAll(
            mapOf(
                1L to Plan(id = 1),
                2L to Plan(id = 2),
                3L to Plan(id = 3)
            )
        )
        val expectedArray = arrayOf(Plan(id = 2))

        repository.delete(listOf(1, 3, 4))
        assertArrayEquals(expectedArray, plansMap.values.toTypedArray())
    }

    @Test
    @DisplayName("deletePlanIfNameIsEmpty(Long)")
    fun deletePlanIfNameIsEmpty() = runTest {
        plansMap.putAll(
            listOf(
                1L to Plan(id = 1L, title = "Test"),
                2L to Plan(id = 2L, title = "")
            )
        )

        var actual = repository.deletePlanIfNameIsEmpty(1L)
        assertFalse(actual)

        actual = repository.deletePlanIfNameIsEmpty(2L)
        assertTrue(actual)
    }

    @Test
    @DisplayName("isEmpty() true")
    fun isEmpty() = runTest {
        plansMap[1] = Plan(id = 1)
        val actual1 = repository.isEmpty()
        assertFalse(actual1)
        plansMap.clear()
        val actual2 = repository.isEmpty()
        assertTrue(actual2)
    }

    @Test
    @DisplayName("clear()")
    fun clear() = runTest {
        plansMap.putAll(
            mapOf(
                1L to Plan(id = 1),
                2L to Plan(id = 2),
                3L to Plan(id = 3)
            )
        )

        repository.clear()
        val actual = plansMap.isEmpty()
        assertTrue(actual)
    }
}