package kanti.tododer.domain.deletetodowithchildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.RepositorySet
import javax.inject.Inject

class DeleteTodoWithProgenyUseCase @Inject constructor(
	private val deletePlanWithProgenyUseCase: DeletePlanWithProgenyUseCase,
	private val deleteTaskWithProgenyUseCase: DeleteTaskWithProgenyUseCase
) {

	suspend operator fun invoke(repositorySet: RepositorySet, todo: Todo) {
		when (todo.type) {
			Todo.Type.PLAN -> deletePlanWithProgenyUseCase(
				repositorySet.planRepository,
				repositorySet.taskRepository,
				todo.id
			)
			Todo.Type.TASK -> deleteTaskWithProgenyUseCase(
				repositorySet.taskRepository,
				todo.id
			)
		}
	}

}