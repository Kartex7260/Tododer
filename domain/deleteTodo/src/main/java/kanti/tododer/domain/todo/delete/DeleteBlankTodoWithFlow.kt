package kanti.tododer.domain.todo.delete

import kanti.tododer.data.model.todo.TodoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBlankTodoWithFlow @Inject constructor(
    private val todoRepository: TodoRepository
) {

    private val _blankTodoDeleted = MutableSharedFlow<Unit>()
    val blankTodoDeleted = _blankTodoDeleted.asSharedFlow()

    suspend operator fun invoke(todoId: Long) {
        val deleted = todoRepository.deleteIfNameIsEmptyAndNoChild(todoId)
        if (deleted)
            _blankTodoDeleted.emit(Unit)
    }
}