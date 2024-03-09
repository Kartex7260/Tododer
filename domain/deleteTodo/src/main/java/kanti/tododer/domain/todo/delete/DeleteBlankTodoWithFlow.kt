package kanti.tododer.domain.todo.delete

import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBlankTodoWithFlow @Inject constructor(
    private val todoRepository: TodoRepository,
    @StandardLog private val logger: Logger
) {

    private val _blankTodoDeleted = MutableSharedFlow<Unit>()
    val blankTodoDeleted = _blankTodoDeleted.asSharedFlow()

    suspend operator fun invoke(todoId: Long) {
        val deleted = todoRepository.deleteIfNameIsEmptyAndNoChild(todoId)
        logger.d(LOG_TAG, "invoke(Long: $todoId): notify subscribers = $deleted")
        if (deleted)
            _blankTodoDeleted.emit(Unit)
    }

    companion object {

        private const val LOG_TAG = "DeleteBlankTodoWithFlow"
    }
}