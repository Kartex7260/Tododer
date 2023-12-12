package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.task.TaskRepository
import javax.inject.Inject

class GetTaskChildrenUseCase @Inject constructor(
	private val taskRepository: kanti.tododer.data.task.TaskRepository
) {

	suspend operator fun invoke(todo: kanti.tododer.data.model.common.Todo): Result<List<kanti.tododer.data.model.common.Todo>> {
		return taskRepository.getChildren(todo.fullId)
	}

}