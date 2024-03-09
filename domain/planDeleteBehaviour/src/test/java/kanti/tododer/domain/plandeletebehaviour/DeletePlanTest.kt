package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.PrintLogger
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock

@ExtendWith(MockitoExtension::class)
class DeletePlanTest {

    private val logTag = "DeletePlanTest"
    private val logger = PrintLogger()

    @AfterEach
    fun afterEach() = runTest {
        logger.d(logTag, "--------- AFTER EACH ---------")
    }

    @Test
    @DisplayName("invoke(List<Long>): don`t invoke")
    fun notInvoke() = runTest {
        var invokeTodoRepoDeleteChildren = false
        var invokePlanRepoDelete = false
        var invokeAppDataRepoDeleteIfCurrent = false
        val planIds = listOf<Long>()
        val todoRepository = mock<TodoRepository> {
            onBlocking { deleteChildren(FullId(1)) } doAnswer {
                invokeTodoRepoDeleteChildren = true
            }
        }
        val planRepository = mock<PlanRepository> {
            onBlocking { delete(planIds) } doAnswer { invokePlanRepoDelete = true }
        }
        val appDataRepository = mock<AppDataRepository> {
            onBlocking { deleteIfCurrent(1) } doAnswer {
                invokeAppDataRepoDeleteIfCurrent = true
            }
        }

        DeletePlan(todoRepository, planRepository, appDataRepository, logger).invoke(planIds)

        assertFalse(invokeTodoRepoDeleteChildren)
        assertFalse(invokePlanRepoDelete)
        assertFalse(invokeAppDataRepoDeleteIfCurrent)
    }

    @Test
    @DisplayName("invoke(List<Long>)")
    fun invoke() = runTest {
        var invokeTodoRepoDeleteChildren = false
        var invokePlanRepoDelete = false
        var invokeAppDataRepoDeleteIfCurrent = false
        val planIds = listOf<Long>(1, 2, 3)
        val todoRepository = mock<TodoRepository> {
            onBlocking { deleteChildren(FullId(1)) } doAnswer {
                invokeTodoRepoDeleteChildren = true
            }
        }
        val planRepository = mock<PlanRepository> {
            onBlocking { delete(planIds) } doAnswer { invokePlanRepoDelete = true }
        }
        val appDataRepository = mock<AppDataRepository> {
            onBlocking { deleteIfCurrent(1) } doAnswer {
                invokeAppDataRepoDeleteIfCurrent = true
            }
        }

        DeletePlan(todoRepository, planRepository, appDataRepository, logger).invoke(planIds)

        assertTrue(invokeTodoRepoDeleteChildren)
        assertTrue(invokePlanRepoDelete)
        assertTrue(invokeAppDataRepoDeleteIfCurrent)
    }
}