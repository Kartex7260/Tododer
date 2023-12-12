package kanti.tododer.domain.removewithchildren

import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.task.TaskRepository
import javax.inject.Inject

class RemoveTaskWithChildrenUseCase @Inject constructor(
	private val taskRepository: kanti.tododer.data.task.TaskRepository
) {

	suspend operator fun invoke(id: Int): Boolean {
		val parentTask = taskRepository.getTask(id)
		val sucParentTask = parentTask.asSuccess ?: return false

		val result = taskRepository.delete(sucParentTask.value)
		deleteChildren(sucParentTask.value.fullId)

		return result.isSuccess
	}

	private suspend fun deleteChildren(fullId: String) {
		val children = taskRepository.getChildren(fullId)
		val sucChildren = children.getOrDefault(listOf())
		for (child in sucChildren) {
			taskRepository.delete(child)
			deleteChildren(child.fullId)
		}
	}

}