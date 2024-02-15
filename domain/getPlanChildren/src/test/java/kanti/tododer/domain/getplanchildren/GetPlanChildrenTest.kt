package kanti.tododer.domain.getplanchildren

import kanti.tododer.common.Const
import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.data.model.plan.toFullId
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetPlanChildrenTest {

    private val logTag = "GetPlanChildrenTest"
    private val logger = PrintLogger()

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "--------- AFTER EACH ---------")
    }

    @Test
    @DisplayName("invoke(Long): not found")
    fun invokeNotFound() = runTest {
        val planId = 1L
        val planRepository = mock<PlanRepository> {
            onBlocking { getPlan(planId) } doReturn null
        }
        val todoRepository = mock<TodoRepository>()
        val getPlanChildren = GetPlanChildren(planRepository, todoRepository, logger)

        val expected = arrayOf<Todo>()
        val actual = getPlanChildren(planId)
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    @DisplayName("invoke(Long): success all")
    fun invokeAll() = runTest {
        val planId = Const.PlansIds.ALL
        val parentPlan = Plan(id = planId, type = PlanType.All)
        val planRepository = mock<PlanRepository> {
            onBlocking { getPlan(planId) } doReturn parentPlan
            onBlocking { getStandardPlans() } doReturn listOf(
                Plan(id = 1L),
                Plan(id = 2L),
                Plan(id = 3L),
                Plan(id = 4L),
                Plan(id = 5L)
            )
        }

        val defFullId = FullId(Const.PlansIds.DEFAULT, FullIdType.Plan)
        val defTodo1 = Todo(id = 1, parentId = defFullId)
        val defTodo2 = Todo(id = 2, parentId = defFullId)

        val p1FullId = FullId(1, FullIdType.Plan)
        val p1Todo1 = Todo(id = 3, parentId = p1FullId)

        val p2FullId = FullId(2, FullIdType.Plan)
        val p2Todo1 = Todo(id = 4, parentId = p2FullId)
        val p2Todo2 = Todo(id = 5, parentId = p2FullId)

        val p3FullId = FullId(3, FullIdType.Plan)

        val p4FullId = FullId(4, FullIdType.Plan)
        val p4Todo1 = Todo(6, parentId = p4FullId)
        val p4Todo2 = Todo(7, parentId = p4FullId)
        val p4Todo3 = Todo(8, parentId = p4FullId)
        val p4Todo4 = Todo(9, parentId = p4FullId)

        val p5FullId = FullId(5, FullIdType.Plan)
        val p5Todo1 = Todo(10, parentId = p5FullId)
        val p5Todo2 = Todo(11, parentId = p5FullId)

        val todoRepository = mock<TodoRepository> {
            onBlocking { getChildren(defFullId) } doReturn listOf(defTodo1, defTodo2)

            onBlocking { getChildren(p1FullId) } doReturn listOf(p1Todo1)

            onBlocking { getChildren(p2FullId) } doReturn listOf(p2Todo1, p2Todo2)

            onBlocking { getChildren(p3FullId) } doReturn listOf()

            onBlocking { getChildren(p4FullId) } doReturn listOf(p4Todo1, p4Todo2, p4Todo3, p4Todo4)

            onBlocking { getChildren(p5FullId) } doReturn listOf(p5Todo1, p5Todo2)
        }
        val getPlanChildren = GetPlanChildren(planRepository, todoRepository, logger)

        val expected = arrayOf(
            defTodo1, defTodo2,
            p1Todo1,
            p2Todo1, p2Todo2,
            p4Todo1, p4Todo2, p4Todo3, p4Todo4,
            p5Todo1, p5Todo2
        )
        val actual = getPlanChildren(planId)
        assertArrayEquals(expected, actual.toTypedArray())
    }

    @Test
    @DisplayName("invoke(Long): success")
    fun invoke() = runTest {
        val planId = 1L
        val plan = Plan(id = planId)
        val planRepository = mock<PlanRepository> {
            onBlocking { getPlan(planId) } doReturn plan
        }
        val todoRepository = mock<TodoRepository> {
            onBlocking { getChildren(plan.toFullId()) } doReturn listOf(
                Todo(id = 3),
                Todo(id = 3524)
            )
        }
        val getPlanChildren = GetPlanChildren(planRepository, todoRepository, logger)

        val expected = arrayOf(
            Todo(id = 3),
            Todo(id = 3524)
        )
        val actual = getPlanChildren(planId)
        assertArrayEquals(expected, actual.toTypedArray())
    }
}