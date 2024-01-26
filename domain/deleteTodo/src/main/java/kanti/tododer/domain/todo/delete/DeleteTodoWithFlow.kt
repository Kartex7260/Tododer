package kanti.tododer.domain.todo.delete

import kanti.tododer.data.model.todo.TodoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class DeleteTodoWithFlow @Inject constructor(
    private val todoRepository: TodoRepository
) {

    private val _blankTodoDeleted = MutableSharedFlow<Unit>()
    val blankTodoDeleted = _blankTodoDeleted.asSharedFlow()

    suspend operator fun invoke(todoId: Long) {
        todoRepository.deleteIfNameIsEmptyAndNoChild(todoId)
        _blankTodoDeleted.emit(Unit)
    }
}