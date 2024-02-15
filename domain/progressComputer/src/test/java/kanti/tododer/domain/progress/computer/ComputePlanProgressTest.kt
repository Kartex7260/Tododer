package kanti.tododer.domain.progress.computer

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.data.model.todo.Todo
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.domain.getplanchildren.GetPlanChildren
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExtendWith(MockitoExtension::class)
class ComputePlanProgressTest {

    private val logger = PrintLogger()

    @Test
    @DisplayName("invoke(Long) todo parent")
    fun invokeParentTodo() = runTest {
        val planId: Long = 1
        val todoParentId = FullId(1, FullIdType.Todo)
        val todoRepository = mock<TodoRepository> {
            onBlocking { getChildren(todoParentId) } doReturn listOf(
                Todo(id = 2, parentId = todoParentId),
                Todo(id = 3, parentId = todoParentId)
            )
        }
        val getPlanChildren = GetPlanChildren(
            planRepository = mock<PlanRepository> {
                onBlocking { getPlan(1) } doReturn Plan(1)
            },
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Plan)) } doReturn listOf(
                    Todo(1, parentId = FullId(1, FullIdType.Plan), done = true)
                )
            },
            logger = logger
        )
        var invokeProgressRepoSetProgress = false
        val progressRepository = mock<ProgressRepository> {
            onBlocking { setProgress(planId, 1f) } doAnswer {
                invokeProgressRepoSetProgress = true
            }
        }
        val expected = 1f

        val computePlanProgress = ComputePlanProgress(
            todoRepository = todoRepository,
            getPlanChildren = getPlanChildren,
            progressRepository = progressRepository,
            logger = logger
        )
        val actual = computePlanProgress(planId)
        assertEquals(expected, actual)
        assertTrue(invokeProgressRepoSetProgress)
    }

    @Test
    @DisplayName("invoke(Long) todo child")
    fun invokeChildTodo() = runTest {
        val planId: Long = 1
        val todoParentId = FullId(1, FullIdType.Todo)
        val todoRepository = mock<TodoRepository> {
            onBlocking { getChildren(todoParentId) } doReturn listOf(
                Todo(id = 2, parentId = todoParentId),
                Todo(id = 3, parentId = todoParentId, done = true),
                Todo(id = 4, parentId = todoParentId)
            )
            onBlocking { getChildren(FullId(2, FullIdType.Todo)) } doReturn listOf()
            onBlocking { getChildren(FullId(3, FullIdType.Todo)) } doReturn listOf()
            onBlocking { getChildren(FullId(4, FullIdType.Todo)) } doReturn listOf()
        }
        val getPlanChildren = GetPlanChildren(
            planRepository = mock<PlanRepository> {
                onBlocking { getPlan(1) } doReturn Plan(1)
            },
            todoRepository = mock<TodoRepository> {
                onBlocking { getChildren(FullId(1, FullIdType.Plan)) } doReturn listOf(
                    Todo(1, parentId = FullId(1, FullIdType.Plan))
                )
            },
            logger = logger
        )
        var invokeProgressRepoSetProgress = false
        val progressRepository = mock<ProgressRepository> {
            onBlocking { setProgress(planId, .25f) } doAnswer {
                invokeProgressRepoSetProgress = true
            }
        }
        val expected = .25f

        val computePlanProgress = ComputePlanProgress(
            todoRepository = todoRepository,
            getPlanChildren = getPlanChildren,
            progressRepository = progressRepository,
            logger = logger
        )
        val actual = computePlanProgress(planId)
        assertEquals(expected, actual)
        assertTrue(invokeProgressRepoSetProgress)
    }
}