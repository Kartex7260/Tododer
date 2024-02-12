package kanti.tododer.data.model.plan.datasource.local

import kanti.sl.StateLanguage
import kanti.sl.serialize
import kanti.tododer.common.Const
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanState
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.room.plan.PlanEntity
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class PlanRoomDataSourceTest {

    private val logTag = "PlanRoomDataSourceTest"
    private val logger = PrintLogger()

    private val sl = StateLanguage { }
    private val stateNormal = sl.serialize(PlanState.Normal as PlanState)

    private val plansMap: MutableMap<Long, PlanEntity> = LinkedHashMap()
    private val dao = FakePlanDao(plansMap, logger)
    private val dataSource: PlanLocalDataSource = PlanRoomDataSource(
        planDao = dao,
        sl = sl,
        logger = logger
    )

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "-------- AFTER EACH --------")
        plansMap.clear()
    }

    @RepeatedTest(3)
    @DisplayName("planAll: Flow<Plan?>")
    fun planAll() = runTest {
        val planAll = Plan(id = Const.PlansIds.ALL, type = PlanType.All)
        val onEachFlow = MutableSharedFlow<Int>()

        launch {
            dataSource.planAll.collectIndexed { index, value ->
                logger.d(logTag, "dataSource.planAll: collect(index: $index, plan: $value)")
                onEachFlow.emit(index)
                when (index) {
                    0 -> {
                        assertNull(value)
                    }

                    1 -> {
                        assertEquals(planAll, value)
                    }

                    2 -> {
                        assertNull(value)
                        cancel("Success")
                    }
                }
            }
        }

        launch {
            onEachFlow.collect { index ->
                when (index) {
                    0 -> {
                        logger.d(logTag, "set plan (all)")
                        plansMap[Const.PlansIds.ALL] = planAll.toPlanEntity(sl)
                        logger.d(logTag, "update flow")
                        dao.updateFlow()
                    }

                    1 -> {
                        logger.d(logTag, "delete plan (all)")
                        plansMap.remove(Const.PlansIds.ALL)
                        logger.d(logTag, "update flow")
                        dao.updateFlow()
                    }

                    2 -> {
                        cancel("Success")
                    }
                }
            }
        }
    }

    @RepeatedTest(3)
    @DisplayName("planDefault: Flow<Plan?>")
    fun planDefault() = runTest {
        val planDefault = Plan(id = Const.PlansIds.DEFAULT, type = PlanType.Default)
        val onEachFlow = MutableSharedFlow<Int>()

        launch {
            dataSource.planDefault.collectIndexed { index, value ->
                logger.d(logTag, "dataSource.planAll: collect(index: $index, plan: $value)")
                onEachFlow.emit(index)
                when (index) {
                    0 -> {
                        assertNull(value)
                    }

                    1 -> {
                        assertEquals(planDefault, value)
                    }

                    2 -> {
                        assertNull(value)
                        cancel("Success")
                    }
                }
            }
        }

        launch {
            onEachFlow.collect { index ->
                when (index) {
                    0 -> {
                        logger.d(logTag, "set plan (all)")
                        plansMap[Const.PlansIds.DEFAULT] = planDefault.toPlanEntity(sl)
                        logger.d(logTag, "update flow")
                        dao.updateFlow()
                    }

                    1 -> {
                        logger.d(logTag, "delete plan (all)")
                        plansMap.remove(Const.PlansIds.DEFAULT)
                        logger.d(logTag, "update flow")
                        dao.updateFlow()
                    }

                    2 -> {
                        cancel("Success")
                    }
                }
            }
        }
    }

    @RepeatedTest(3)
    @DisplayName("standardPlans: Flow<List<Plan>>")
    fun standardPlans() = runTest {
        val onEachFlow = MutableSharedFlow<Int>()
        val expected = arrayOf(
            Plan(id = 2, state = PlanState.Normal, type = PlanType.Custom),
            Plan(id = 4, state = PlanState.Normal, type = PlanType.Custom)
        )

        launch {
            dataSource.standardPlans.collectIndexed { index, plans ->
                logger.d(
                    logTag,
                    "dataSource.standardPlans: collect (index: $index, plans: count(${plans.size}))"
                )
                onEachFlow.emit(index)
                when (index) {
                    0 -> {
                        assertArrayEquals(arrayOf(), plans.toTypedArray())
                    }

                    1 -> {
                        assertArrayEquals(expected, plans.toTypedArray())
                        cancel("Success")
                    }
                }
            }
        }

        launch {
            onEachFlow.collect { index ->
                when (index) {
                    0 -> {
                        plansMap.putAll(
                            mapOf(
                                1L to PlanEntity(
                                    id = 1,
                                    state = stateNormal,
                                    type = PlanType.All.toString()
                                ),
                                2L to PlanEntity(
                                    id = 2,
                                    state = stateNormal,
                                    type = PlanType.Custom.toString()
                                ),
                                3L to PlanEntity(
                                    id = 3,
                                    state = stateNormal,
                                    type = PlanType.Default.toString()
                                ),
                                4L to PlanEntity(
                                    id = 4,
                                    state = stateNormal,
                                    type = PlanType.Custom.toString()
                                )
                            )
                        )
                        dao.updateFlow()
                    }

                    1 -> {
                        cancel("Success")
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("getPlan(Long)")
    fun getPlan() = runTest {
        val planId = 4L
        val expected = Plan(id = planId)

        var actual = dataSource.getPlan(planId)
        assertNull(actual)

        plansMap[planId] = expected.toPlanEntity(sl)
        actual = dataSource.getPlan(planId)
        assertEquals(expected, actual)

        plansMap.remove(planId)
        actual = dataSource.getPlan(planId)
        assertNull(actual)
    }

    @Test
    @DisplayName("getPlanFromType(PlanType)")
    fun getPlanFromType() = runTest {
        val planId = 3L
        val type = PlanType.Default
        val expected = Plan(id = planId, type = PlanType.Default)

        var actual = dataSource.getPlanFromType(type)
        assertNull(actual)

        plansMap[planId] = expected.toPlanEntity(sl)
        actual = dataSource.getPlanFromType(type)
        assertEquals(expected, actual)

        plansMap[54L] = Plan(id = 54L, type = PlanType.Custom).toPlanEntity(sl)
        actual = dataSource.getPlanFromType(PlanType.Custom)
        assertNull(actual)
    }

    @Test
    @DisplayName("getPlans(List<Long>)")
    fun getPlans() = runTest {
        val planIds = listOf(3L, 5L, 90L)

        var expected = arrayOf<Plan>()
        var actual = dataSource.getPlans(planIds)
        assertArrayEquals(expected, actual.toTypedArray())

        plansMap.putAll(
            mapOf(
                3L to Plan(id = 3L).toPlanEntity(sl),
                6L to Plan(id = 6L).toPlanEntity(sl),
                90L to Plan(id = 90L).toPlanEntity(sl)
            )
        )
        expected = arrayOf(
            Plan(id = 3L),
            Plan(id = 90L)
        )
        actual = dataSource.getPlans(planIds)
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    @DisplayName("getStandardPlans()")
    fun getStandardPlans() = runTest {
        var expected = arrayOf<Plan>()
        var actual = dataSource.getStandardPlans()
        assertArrayEquals(expected, actual.toTypedArray())

        plansMap.putAll(
            mapOf(
                1L to Plan(id = 1L, type = PlanType.All).toPlanEntity(sl),
                2L to Plan(id = 2L, type = PlanType.Default).toPlanEntity(sl),
                3L to Plan(id = 3L, type = PlanType.Custom).toPlanEntity(sl),
                4L to Plan(id = 4L, type = PlanType.Custom).toPlanEntity(sl)
            )
        )
        expected = arrayOf(Plan(id = 3L), Plan(id = 4L))
        actual = dataSource.getStandardPlans()
        assertArrayEquals(expected, actual.toTypedArray())

        plansMap.remove(3L)
        plansMap.remove(4L)

        expected = arrayOf()
        actual = dataSource.getStandardPlans()
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    @DisplayName("insert(Plan)")
    fun insert() = runTest {
        plansMap.putAll(
            mapOf(
                1L to Plan(id = 1).toPlanEntity(sl),
                2L to Plan(id = 2).toPlanEntity(sl)
            )
        )

        var expected = 3L
        var actual = dataSource.insert(Plan(title = "Test"))
        assertEquals(expected, actual)
        assertArrayEquals(
            arrayOf(
                Plan(id = 1L).toPlanEntity(sl),
                Plan(id = 2L).toPlanEntity(sl),
                Plan(id = 3L, title = "Test").toPlanEntity(sl)
            ),
            plansMap.values.toTypedArray()
        )

        expected = -1
        actual = dataSource.insert(Plan(id = 1, title = "Foo"))
        assertEquals(expected, actual)
        assertArrayEquals(
            arrayOf(
                Plan(id = 1L).toPlanEntity(sl),
                Plan(id = 2L).toPlanEntity(sl),
                Plan(id = 3L, title = "Test").toPlanEntity(sl)
            ),
            plansMap.values.toTypedArray()
        )
    }

    @Test
    @DisplayName("insert(List<Plan>)")
    fun insertList() = runTest {
        dataSource.insert(
            listOf(
                Plan(title = "Test 1"),
                Plan(title = "Test 2")
            )
        )
        assertArrayEquals(
            arrayOf(
                Plan(id = 1L, title = "Test 1").toPlanEntity(sl),
                Plan(id = 2L, title = "Test 2").toPlanEntity(sl)
            ),
            plansMap.values.toTypedArray()
        )

        dataSource.insert(
            listOf(
                Plan(id = 2L, title = "Test 3"),
                Plan(id = 3L, title = "Test 4")
            )
        )
        assertArrayEquals(
            arrayOf(
                Plan(id = 1L, title = "Test 1").toPlanEntity(sl),
                Plan(id = 2L, title = "Test 2").toPlanEntity(sl),
                Plan(id = 3L, title = "Test 4").toPlanEntity(sl)
            ),
            plansMap.values.toTypedArray()
        )
    }

    @Test
    @DisplayName("update(Plan)")
    fun updateTitle() = runTest {
        plansMap.putAll(
            mapOf(
                1L to Plan(id = 1, title = "Test 1").toPlanEntity(sl),
                2L to Plan(id = 2, title = "Test 2").toPlanEntity(sl),
                3L to Plan(id = 3, title = "Test 3").toPlanEntity(sl)
            )
        )
        val expectedArray = arrayOf(
            Plan(id = 1, title = "Test 1").toPlanEntity(sl),
            Plan(id = 2, title = "Updated").toPlanEntity(sl),
            Plan(id = 3, title = "Test 3").toPlanEntity(sl)
        )

        dataSource.updateTitle(2, "Updated")
        assertArrayEquals(expectedArray, plansMap.values.toTypedArray())
    }

    @Test
    @DisplayName("delete(List<Plan>)")
    fun delete() = runTest {
        plansMap.putAll(
            mapOf(
                1L to PlanEntity(
                    id = 1,
                    state = stateNormal,
                    title = "Test 1",
                    type = PlanType.All.toString()
                ),
                2L to PlanEntity(
                    id = 2,
                    state = stateNormal,
                    title = "Test 2",
                    type = PlanType.Default.toString()
                ),
                3L to PlanEntity(
                    id = 3,
                    state = stateNormal,
                    title = "Test 3",
                    type = PlanType.Custom.toString()
                ),
                4L to PlanEntity(
                    id = 4,
                    state = stateNormal,
                    title = "Test 4",
                    type = PlanType.Custom.toString()
                )
            )
        )
        val expected = arrayOf(
            PlanEntity(
                id = 1,
                state = stateNormal,
                title = "Test 1",
                type = PlanType.All.toString()
            ),
            PlanEntity(
                id = 2,
                state = stateNormal,
                title = "Test 2",
                type = PlanType.Default.toString()
            )
        )

        dataSource.delete(listOf(3, 4, 5))

        assertArrayEquals(expected, plansMap.values.toTypedArray())
    }

    @Test
    @DisplayName("deletePlanIfNameIsEmpty(Long)")
    fun deletePlanIfNameIsEmpty() = runTest {
        val planId = 1L
        var actual = dataSource.deletePlanIfNameIsEmpty(planId)
        assertFalse(actual)
        assertTrue(plansMap.isEmpty())

        plansMap[planId] = Plan(id = planId, title = "Test").toPlanEntity(sl)
        actual = dataSource.deletePlanIfNameIsEmpty(planId)
        assertFalse(actual)
        assertTrue(plansMap.isNotEmpty())

        plansMap[planId] = Plan(id = planId).toPlanEntity(sl)
        actual = dataSource.deletePlanIfNameIsEmpty(planId)
        assertTrue(actual)
        assertTrue(plansMap.isEmpty())
    }

    @Test
    @DisplayName("isEmpty()")
    fun isEmpty() = runTest {
        var actual = dataSource.isEmpty()
        assertTrue(actual)

        plansMap[1L] = Plan(id = 1L).toPlanEntity(sl)
        actual = dataSource.isEmpty()
        assertFalse(actual)
    }

    @Test
    @DisplayName("clear()")
    fun clear() = runTest {
        plansMap.putAll(
            mapOf(
                1L to PlanEntity(
                    id = 1,
                    state = stateNormal,
                    title = "Test 1",
                    type = PlanType.All.toString()
                ),
                2L to PlanEntity(
                    id = 2,
                    state = stateNormal,
                    title = "Test 2",
                    type = PlanType.Default.toString()
                ),
                3L to PlanEntity(
                    id = 3,
                    state = stateNormal,
                    title = "Test 3",
                    type = PlanType.Custom.toString()
                ),
                4L to PlanEntity(
                    id = 4,
                    state = stateNormal,
                    title = "Test 4",
                    type = PlanType.Custom.toString()
                )
            )
        )
        dataSource.clear()

        assertTrue(plansMap.isEmpty())
    }
}