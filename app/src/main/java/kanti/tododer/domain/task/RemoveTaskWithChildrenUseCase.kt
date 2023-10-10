package kanti.tododer.domain.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.fullId
import javax.inject.Inject

class RemoveTaskWithChildrenUseCase @Inject constructor(
	private val taskRepository: ITaskRepository
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