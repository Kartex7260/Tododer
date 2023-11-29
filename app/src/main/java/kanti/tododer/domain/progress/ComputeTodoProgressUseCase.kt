package kanti.tododer.domain.progress

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.data.model.progress.TodoProgress
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.asTask
import kanti.tododer.domain.gettodochildren.GetTodoChildrenUseCase
import kanti.tododer.data.model.RepositorySet
import javax.inject.Inject

class ComputeTodoProgressUseCase @Inject constructor(
	private val getTodoChildrenUseCase: GetTodoChildrenUseCase
) {

	suspend operator fun invoke(
		todoProgressRepository: TodoProgressRepository,
		repositorySet: RepositorySet,
		todo: Todo
	): Float {
		return when(todo.type) {
			Todo.Type.TASK -> {
				computeTask(
					todoProgressRepository,
					repositorySet,
					todo.asTask
				)
			}
			Todo.Type.PLAN -> {
				computePlan(
					todoProgressRepository,
					repositorySet,
					todo.asPlan
				)
			}
		}
	}

	private suspend fun computeTask(
		todoProgressRepository: TodoProgressRepository,
		repositorySet: RepositorySet,
		task: Task
	): Float {
		if (task.done)
			return 1f

		return computeChildren(
			todoProgressRepository,
			repositorySet,
			task
		)
	}

	private suspend fun computePlan(
		todoProgressRepository: TodoProgressRepository,
		repositorySet: RepositorySet,
		planImpl: Plan
	): Float {
		val progress = computeChildren(
			todoProgressRepository,
			repositorySet,
			planImpl
		)
		todoProgressRepository.insert(
			TodoProgress(
				planImpl.fullId,
				progress
			)
		)
		return progress
	}

	private suspend fun computeChildren(
		todoProgressRepository: TodoProgressRepository,
		repositorySet: RepositorySet,
		todo: Todo
	): Float {
		val children = getTodoChildrenUseCase(repositorySet, todo)
		if (children.isEmpty())
			return 0f

		var result = 0f
		var count = 0
		for (child in children) {
			result += invoke(
				todoProgressRepository,
				repositorySet,
				child
			)
			count++
		}

		return result / count
	}

}
