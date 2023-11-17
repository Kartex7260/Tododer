package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class GetTaskChildrenUseCase @Inject constructor() {

	suspend operator fun invoke(
		taskRepository: TaskRepository,
		todo: Todo
	): List<Todo> {
		val repositoryResult = taskRepository.getChildren(todo.fullId)
		return repositoryResult.value ?: listOf()
	}

}