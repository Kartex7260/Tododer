package kanti.tododer.domain.task.taskwithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import javax.inject.Inject

class GetTaskWithChildrenUseCase @Inject constructor(
	private val taskRepository: ITaskRepository
) {

	suspend operator fun invoke(id: Int): RepositoryResult<TaskWithChildren> {
		val repositoryResult = taskRepository.getTask(id)

		val task = repositoryResult.value
		val childTasks = getChildTask(task?.fullId)

		val taskWithChildren = TaskWithChildren(
			task,
			childTasks
		)
		return RepositoryResult(taskWithChildren, repositoryResult.type)
	}

	private suspend fun getChildTask(fid: String?): List<Task> {
		if (fid == null)
			return listOf()
		val repositoryResult = taskRepository.getChildren(fid)
		return repositoryResult.value ?: listOf()
	}

}