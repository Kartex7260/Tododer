package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.task.ITaskRepository
import javax.inject.Inject

class GetTaskChildrenUseCase @Inject constructor(
	private val taskRepository: ITaskRepository
) {

	suspend operator fun invoke(todo: Todo): List<Todo> {
		val repositoryResult = taskRepository.getChildren(todo.fullId)
		return repositoryResult.value ?: listOf()
	}

}