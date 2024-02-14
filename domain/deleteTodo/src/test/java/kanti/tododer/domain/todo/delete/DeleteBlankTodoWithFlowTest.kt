package kanti.tododer.domain.todo.delete

import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.PrintLogger
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExtendWith(MockitoExtension::class)
class DeleteBlankTodoWithFlowTest {

    private val logTag = "DeleteBlankTodoWithFlowTest"
    private val logger = PrintLogger()

    private val todoId: Long = 1

    @RepeatedTest(5)
    @DisplayName("invoke(Long)")
    fun invoke() = runTest {
        val repository = mock<TodoRepository> {
            onBlocking { deleteIfNameIsEmptyAndNoChild(todoId) } doReturn true
        }
        val deleteBlankTodoWithFlow = DeleteBlankTodoWithFlow(repository, logger)

        launch {
            logger.d(logTag, "start collecting deleteBlankTodoWithFlow.blankTodoDeleted")
            deleteBlankTodoWithFlow.blankTodoDeleted.collect {
                logger.d(logTag, "deleteBlankTodoWithFlow.blankTodoDeleted: collected")
                cancel("Success")
            }
        }

        delay(100L)
        logger.d(logTag, "invoke deleteBlankTodoWithFlow(Long = $todoId)")
        deleteBlankTodoWithFlow(todoId)
    }
}