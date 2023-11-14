package kanti.tododer.domain.deletetodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class DeleteTaskWithProgenyUseCase @Inject constructor() {

	suspend operator fun invoke(
		taskRepository: TaskRepository,
		id: Int
	): Boolean {
		val parentTask = taskRepository.getTask(id)
		if (parentTask.type is RepositoryResult.Type.NotFound || parentTask.value == null)
			return false

		val result = taskRepository.delete(parentTask.value)
		deleteChildren(taskRepository, parentTask.value.fullId)

		return result
	}

	private suspend fun deleteChildren(
		taskRepository: TaskRepository,
		fullId: String
	) {
		val children = taskRepository.getChildren(fullId)
		for (child in children.value!!) {
			taskRepository.delete(child)
			deleteChildren(taskRepository, child.fullId)
		}
	}

}