package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetTaskChildrenUseCase
import javax.inject.Inject

class GetTaskWithChildrenUseCase @Inject constructor(
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(
		taskRepository: TaskRepository,
		id: Int
	): RepositoryResult<TodoWithChildren> {
		val repositoryResult = taskRepository.getTask(id)

		val task = repositoryResult.value
		val childTasks = if (task != null)
			getTaskChildrenUseCase(
				taskRepository,
				task.toFullId
			)
		else
			listOf()

		val taskWithChildren = TodoWithChildren(
			task,
			childTasks
		)
		return RepositoryResult(taskWithChildren, repositoryResult.type)
	}

}