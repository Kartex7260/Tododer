package kanti.tododer.domain.progress.computer

import kanti.tododer.common.Const
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.progress.PlanProgress
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExtendWith(MockitoExtension::class)
class GetProgressFromAllPlanTest {

    private val logTag = "GetProgressFromAllPlanTest"
    private val logger = PrintLogger()

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "-------- AFTER EACH --------")
    }

    @Test
    @DisplayName("planAllProgress: Flow<Float>: no cached")
    fun planAllProgressNoCached() = runTest {
        val planId = Const.PlansIds.ALL
        val planType = PlanType.All
        val plan = Plan(id = planId, type = planType)
        val planRepository = mock<PlanRepository> {
            onBlocking { planAll } doReturn flowOf(plan)
        }
        val progressRepository = mock<ProgressRepository> {
            onBlocking { getProgress(planId) } doReturn null
        }
        var invokeProgressRepoSetProgress = false
        val computePlanProgress = ComputePlanProgress(
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Todo)) } doReturn listOf()
            },
            getPlanChildren = GetPlanChildren(
                planRepository = mock<PlanRepository> {
                    onBlocking { getPlan(planId) } doReturn plan
                    onBlocking { getStandardPlans() } doReturn listOf(Plan(id = 1L))
                },
                todoRepository = mock<TodoRepository> {
                    onBlocking { getChildren(FullId(Const.PlansIds.DEFAULT)) } doReturn listOf(
                        Todo(1, parentId = FullId(Const.PlansIds.DEFAULT))
                    )
                    onBlocking { getChildren(FullId(1L)) } doReturn listOf(
                        Todo(2, parentId = FullId(1L), done = true)
                    )
                },
                logger = logger
            ),
            progressRepository = mock<ProgressRepository> {
                onBlocking { setProgress(planId, .5f) } doAnswer {
                    invokeProgressRepoSetProgress = true
                }
            },
            logger = logger
        )
        val getProgressFromAllPlan = GetProgressFromAllPlan(
            planRepository = planRepository,
            progressRepository = progressRepository,
            computePlanProgress = computePlanProgress,
            logger = logger
        )

        val onEach = MutableSharedFlow<Int>()
        launch {
            getProgressFromAllPlan.planAllProgress.collectIndexed { index, progress ->
                onEach.emit(index)
                when (index) {
                    0 -> assertEquals(0f, progress)
                    1 -> {
                        assertEquals(.5f, progress)
                        cancel("Success")
                    }
                }
            }
        }
        launch {
            onEach.collect { index ->
                when (index) {
                    1 -> {
                        assertTrue(invokeProgressRepoSetProgress)
                        cancel("Success")
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("planAllProgress: Flow<Float>: cached")
    fun planAllProgressCached() = runTest {
        val planId = Const.PlansIds.ALL
        val planType = PlanType.All
        val plan = Plan(id = planId, type = planType)
        val planRepository = mock<PlanRepository> {
            onBlocking { planAll } doReturn flowOf(plan)
        }
        val progressRepository = mock<ProgressRepository> {
            onBlocking { getProgress(planId) } doReturn .33f
        }
        var invokeProgressRepoSetProgress = false
        val computePlanProgress = ComputePlanProgress(
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Todo)) } doReturn listOf()
            },
            getPlanChildren = GetPlanChildren(
                planRepository = mock<PlanRepository> {
                    onBlocking { getPlan(planId) } doReturn plan
                    onBlocking { getStandardPlans() } doReturn listOf(Plan(id = 1L))
                },
                todoRepository = mock<TodoRepository> {
                    onBlocking { getChildren(FullId(Const.PlansIds.DEFAULT)) } doReturn listOf(
                        Todo(1, parentId = FullId(Const.PlansIds.DEFAULT))
                    )
                    onBlocking { getChildren(FullId(1L)) } doReturn listOf(
                        Todo(2, parentId = FullId(1L), done = true)
                    )
                },
                logger = logger
            ),
            progressRepository = mock<ProgressRepository> {
                onBlocking { setProgress(planId, .5f) } doAnswer {
                    invokeProgressRepoSetProgress = true
                }
            },
            logger = logger
        )
        val getProgressFromAllPlan = GetProgressFromAllPlan(
            planRepository = planRepository,
            progressRepository = progressRepository,
            computePlanProgress = computePlanProgress,
            logger = logger
        )

        val onEach = MutableSharedFlow<Int>()
        launch {
            getProgressFromAllPlan.planAllProgress.collectIndexed { index, progress ->
                onEach.emit(index)
                when (index) {
                    0 -> assertEquals(.33f, progress)
                    1 -> {
                        assertEquals(.5f, progress)
                        cancel("Success")
                    }
                }
            }
        }
        launch {
            onEach.collect { index ->
                when (index) {
                    1 -> {
                        assertTrue(invokeProgressRepoSetProgress)
                        cancel("Success")
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("planDefaultProgress: Flow<Float>: no cached")
    fun planDefaultProgressNoCached() = runTest {
        val planId = Const.PlansIds.DEFAULT
        val planType = PlanType.Default
        val plan = Plan(id = planId, type = planType)
        val planRepository = mock<PlanRepository> {
            onBlocking { planDefault } doReturn flowOf(plan)
        }
        val progressRepository = mock<ProgressRepository> {
            onBlocking { getProgress(planId) } doReturn null
        }
        var invokeProgressRepoSetProgress = false
        val computePlanProgress = ComputePlanProgress(
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Todo)) } doReturn listOf()
            },
            getPlanChildren = GetPlanChildren(
                planRepository = mock<PlanRepository> {
                    onBlocking { getPlan(planId) } doReturn plan
                },
                todoRepository = mock<TodoRepository> {
                    onBlocking { getChildren(FullId(planId)) } doReturn listOf(
                        Todo(1, parentId = FullId(planId)),
                        Todo(2, parentId = FullId(planId), done = true)
                    )
                },
                logger = logger
            ),
            progressRepository = mock<ProgressRepository> {
                onBlocking { setProgress(planId, .5f) } doAnswer {
                    invokeProgressRepoSetProgress = true
                }
            },
            logger = logger
        )
        val getProgressFromAllPlan = GetProgressFromAllPlan(
            planRepository = planRepository,
            progressRepository = progressRepository,
            computePlanProgress = computePlanProgress,
            logger = logger
        )

        val onEach = MutableSharedFlow<Int>()
        launch {
            getProgressFromAllPlan.planDefaultProgress.collectIndexed { index, progress ->
                onEach.emit(index)
                when (index) {
                    0 -> assertEquals(0f, progress)
                    1 -> {
                        assertEquals(.5f, progress)
                        cancel("Success")
                    }
                }
            }
        }
        launch {
            onEach.collect { index ->
                when (index) {
                    1 -> {
                        assertTrue(invokeProgressRepoSetProgress)
                        cancel("Success")
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("planDefaultProgress: Flow<Float>: cached")
    fun planDefaultProgressCached() = runTest {
        val planId = Const.PlansIds.DEFAULT
        val planType = PlanType.Default
        val plan = Plan(id = planId, type = planType)
        val planRepository = mock<PlanRepository> {
            onBlocking { planDefault } doReturn flowOf(plan)
        }
        val progressRepository = mock<ProgressRepository> {
            onBlocking { getProgress(planId) } doReturn .33f
        }
        var invokeProgressRepoSetProgress = false
        val computePlanProgress = ComputePlanProgress(
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Todo)) } doReturn listOf()
            },
            getPlanChildren = GetPlanChildren(
                planRepository = mock<PlanRepository> {
                    onBlocking { getPlan(planId) } doReturn plan
                },
                todoRepository = mock<TodoRepository> {
                    onBlocking { getChildren(FullId(planId)) } doReturn listOf(
                        Todo(1, parentId = FullId(planId)),
                        Todo(2, parentId = FullId(planId), done = true)
                    )
                },
                logger = logger
            ),
            progressRepository = mock<ProgressRepository> {
                onBlocking { setProgress(planId, .5f) } doAnswer {
                    invokeProgressRepoSetProgress = true
                }
            },
            logger = logger
        )
        val getProgressFromAllPlan = GetProgressFromAllPlan(
            planRepository = planRepository,
            progressRepository = progressRepository,
            computePlanProgress = computePlanProgress,
            logger = logger
        )

        val onEach = MutableSharedFlow<Int>()
        launch {
            getProgressFromAllPlan.planDefaultProgress.collectIndexed { index, progress ->
                onEach.emit(index)
                when (index) {
                    0 -> assertEquals(.33f, progress)
                    1 -> {
                        assertEquals(.5f, progress)
                        cancel("Success")
                    }
                }
            }
        }
        launch {
            onEach.collect { index ->
                when (index) {
                    1 -> {
                        assertTrue(invokeProgressRepoSetProgress)
                        cancel("Success")
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("plansProgress: Flow<PlanProgress>: no cached")
    fun plansProgress() = runTest {
        val planId1 = 1L
        val plan1 = Plan(id = planId1)
        val planId2 = 2L
        val plan2 = Plan(id = planId2)
        val planRepository = mock<PlanRepository> {
            onBlocking { standardPlans } doReturn flowOf(listOf(plan1, plan2))
        }
        val progressRepository = mock<ProgressRepository> {
            onBlocking { getProgress(planId1) } doReturn null
            onBlocking { getProgress(planId2) } doReturn .33f
        }
        var invokeProgressRepoSetProgress1 = false
        var invokeProgressRepoSetProgress2 = false
        val computePlanProgress = ComputePlanProgress(
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Todo)) } doReturn listOf()
                onBlocking { getChildren(FullId(3, FullIdType.Todo)) } doReturn listOf()
                onBlocking { getChildren(FullId(4, FullIdType.Todo)) } doReturn listOf()
                onBlocking { getChildren(FullId(6, FullIdType.Todo)) } doReturn listOf()
            },
            getPlanChildren = GetPlanChildren(
                planRepository = mock<PlanRepository> {
                    onBlocking { getPlan(planId1) } doReturn plan1
                    onBlocking { getPlan(planId2) } doReturn plan2
                },
                todoRepository = mock<TodoRepository> {
                    onBlocking { getChildren(FullId(planId1)) } doReturn listOf(
                        Todo(1, parentId = FullId(planId1)),
                        Todo(2, parentId = FullId(planId1), done = true)
                    )
                    onBlocking { getChildren(FullId(planId2)) } doReturn listOf(
                        Todo(3, parentId = FullId(planId2)),
                        Todo(4, parentId = FullId(planId2)),
                        Todo(5, parentId = FullId(planId2), done = true),
                        Todo(6, parentId = FullId(planId2))
                    )
                },
                logger = logger
            ),
            progressRepository = mock<ProgressRepository> {
                onBlocking { setProgress(planId1, .5f) } doAnswer {
                    invokeProgressRepoSetProgress1 = true
                }
                onBlocking { setProgress(planId2, .25f) } doAnswer {
                    invokeProgressRepoSetProgress2 = true
                }
            },
            logger = logger
        )
        val getProgressFromAllPlan = GetProgressFromAllPlan(
            planRepository = planRepository,
            progressRepository = progressRepository,
            computePlanProgress = computePlanProgress,
            logger = logger
        )

        val progressRegister = ProgressRegister()
        val onEach = MutableSharedFlow<Int>()
        launch {
            getProgressFromAllPlan.plansProgress.collectIndexed { index, progress ->
                onEach.emit(index)
                progressRegister.register(progress)
                if (index == 3) {
                    assertTrue(progressRegister.allRegistered)
                    cancel("Success")
                }
            }
        }
        launch {
            onEach.collect { index ->
                when (index) {
                    3 -> {
                        assertTrue(invokeProgressRepoSetProgress1)
                        assertTrue(invokeProgressRepoSetProgress2)
                        cancel("Success")
                    }
                }
            }
        }
    }
}

private class ProgressRegister {

    private val progresses: MutableMap<Long, Int> = LinkedHashMap()
    private val mutex = Mutex()

    val allRegistered: Boolean get() {
        val sum = progresses.values.reduce { acc, i -> acc + i }
        val count = progresses.size
        return sum / count == 2
    }

    suspend fun register(progress: PlanProgress) {
        mutex.withLock {
            progresses[progress.planId] = progresses[progress.planId]?.plus(1) ?: 1
        }
    }
}