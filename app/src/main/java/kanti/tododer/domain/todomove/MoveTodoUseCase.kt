package kanti.tododer.domain.todomove

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.RepositorySet
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.task.asTask
import javax.inject.Inject

class MoveTodoUseCase @Inject constructor(
	private val moveTaskUseCase: MoveTaskUseCase,
	private val movePlanUseCase: MovePlanUseCase
) {

	suspend operator fun invoke(
		from: RepositorySet,
		to: RepositorySet,
		todo: Todo
	): RepositoryResult<Unit> {
		return when (todo.type) {
			Todo.Type.TASK -> moveTaskUseCase(from.taskRepository, to.taskRepository, todo.asTask)
			Todo.Type.PLAN -> movePlanUseCase(from, to, todo.asPlan)
		}
	}

}