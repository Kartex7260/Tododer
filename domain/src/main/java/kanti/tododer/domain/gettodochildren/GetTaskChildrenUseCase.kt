package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.TaskRepository
import javax.inject.Inject

class GetTaskChildrenUseCase @Inject constructor(
	private val taskRepository: TaskRepository
) {

	suspend operator fun invoke(todo: Todo): Result<List<Todo>> {
		return taskRepository.getChildren(todo.fullId)
	}

}