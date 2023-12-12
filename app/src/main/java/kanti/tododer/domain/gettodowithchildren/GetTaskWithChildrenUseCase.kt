package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.model.common.result.toAnotherGenericType
import kanti.tododer.data.task.TaskRepository
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetTaskChildrenUseCase
import javax.inject.Inject

class GetTaskWithChildrenUseCase @Inject constructor(
	private val taskRepository: kanti.tododer.data.task.TaskRepository,
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(id: Int): GetRepositoryResult<TodoWithChildren> {
		val repositoryResult = taskRepository.getTask(id)
		val successResult = repositoryResult.asSuccess
			?: return repositoryResult.toAnotherGenericType()

		val childTasks = getTaskChildrenUseCase(successResult.value)
		if (childTasks.isFailure) {
			val th = childTasks.exceptionOrNull()
			return GetRepositoryResult.Fail(th?.message, th)
		}

		val taskWithChildren = TodoWithChildren(
			successResult.value,
			childTasks.getOrDefault(listOf())
		)
		return GetRepositoryResult.Success(taskWithChildren)
	}

}