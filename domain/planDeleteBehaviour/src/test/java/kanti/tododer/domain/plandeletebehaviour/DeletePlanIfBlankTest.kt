package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.FullId
import kanti.tododer.data.model.FullIdType
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.PrintLogger
import kanti.todoer.data.appdata.AppDataRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExtendWith(MockitoExtension::class)
class DeletePlanIfBlankTest {

    private val logger = PrintLogger()

    @RepeatedTest(5)
    @DisplayName("invoke(FullId)")
    fun invoke() = runTest {
        val planId = 1L
        val planFullId = FullId(planId, FullIdType.Plan)
        val planRepository = mock<PlanRepository> {
            onBlocking { deletePlanIfNameIsEmpty(planId) } doReturn true
        }
        val todoRepository = mock<TodoRepository> {
            onBlocking { getChildrenCount(planFullId) } doReturn 0
        }
        var invokeAppDataRepoDeleteIfCurrent = false
        val appDataRepository = mock<AppDataRepository> {
            onBlocking { deleteIfCurrent(planId) } doAnswer {
                invokeAppDataRepoDeleteIfCurrent = true
            }
        }

        val deletePlanIfBlank = DeletePlanIfBlank(
            planRepository,
            todoRepository,
            appDataRepository,
            logger
        )

        launch {
            deletePlanIfBlank.planDeleted.collect {
                assertTrue(invokeAppDataRepoDeleteIfCurrent)
                cancel("success")
            }
        }

        delay(100L)
        deletePlanIfBlank(planFullId)
    }
}