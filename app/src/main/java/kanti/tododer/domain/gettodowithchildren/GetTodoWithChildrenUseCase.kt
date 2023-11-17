package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.FullId
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetTodoWithChildrenUseCase @Inject constructor(
	private val getPlanWithChildrenUseCase: GetPlanWithChildrenUseCase,
	private val getTaskWithChildrenUseCase: GetTaskWithChildrenUseCase
) {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		fullId: FullId
	): RepositoryResult<TodoWithChildren> {
		return when (fullId.type) {
			Todo.Type.PLAN -> getPlanWithChildrenUseCase(
				repositorySet,
				fullId.id
			)
			Todo.Type.TASK -> getTaskWithChildrenUseCase(
				repositorySet.taskRepository,
				fullId.id
			)
		}
	}

}