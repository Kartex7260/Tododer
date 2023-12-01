package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.common.toFullId
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetTaskChildrenUseCase
import kanti.tododer.domain.gettodochildren.GetTodoChildrenUseCase
import javax.inject.Inject

class GetTaskWithChildrenUseCase @Inject constructor(
	private val taskRepository: ITaskRepository,
	private val getTaskChildrenUseCase: GetTaskChildrenUseCase
) {

	suspend operator fun invoke(id: Int): RepositoryResult<TodoWithChildren> {
		val repositoryResult = taskRepository.getTask(id)

		val task = repositoryResult.value
		val childTasks = if (task != null)
			getTaskChildrenUseCase(task.toFullId)
		else
			listOf()

		val taskWithChildren = TodoWithChildren(
			task,
			childTasks
		)
		return RepositoryResult(taskWithChildren, repositoryResult.type)
	}

}