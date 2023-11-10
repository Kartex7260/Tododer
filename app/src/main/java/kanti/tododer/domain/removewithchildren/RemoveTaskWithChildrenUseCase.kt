package kanti.tododer.domain.removewithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class RemoveTaskWithChildrenUseCase @Inject constructor(
	@StandardDataQualifier private val taskRepository: TaskRepository
) {

	suspend operator fun invoke(id: Int): Boolean {
		val parentTask = taskRepository.getTask(id)
		if (parentTask.type is RepositoryResult.Type.NotFound || parentTask.value == null)
			return false

		val result = taskRepository.delete(parentTask.value)
		deleteChildren(parentTask.value.fullId)

		return result
	}

	private suspend fun deleteChildren(fullId: String) {
		val children = taskRepository.getChildren(fullId)
		for (child in children.value!!) {
			taskRepository.delete(child)
			deleteChildren(child.fullId)
		}
	}

}