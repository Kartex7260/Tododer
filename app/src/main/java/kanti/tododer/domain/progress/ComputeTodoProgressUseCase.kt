package kanti.tododer.domain.progress

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.asPlan
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.asTask
import kanti.tododer.data.progress.TodoProgressRepository
import kanti.tododer.data.progress.TodoProgress
import kanti.tododer.domain.gettodochildren.GetTodoChildrenUseCase
import javax.inject.Inject

class ComputeTodoProgressUseCase @Inject constructor(
	private val todoProgressRepository: TodoProgressRepository,
	private val getTodoChildrenUseCase: GetTodoChildrenUseCase
) {

	suspend operator fun invoke(todo: Todo): Float {
		return when(todo.type) {
			Todo.Type.TASK -> {
				computeTask(todo.asTask)
			}
			Todo.Type.PLAN -> {
				computePlan(todo.asPlan)
			}
		}
	}

	private suspend fun computeTask(task: Task): Float {
		if (task.done)
			return 1f

		return computeChildren(task)
	}

	private suspend fun computePlan(plan: Plan): Float {
		val progress = computeChildren(plan)
		todoProgressRepository.insert(
			TodoProgress(
				plan.fullId,
				progress
			)
		)
		return progress
	}

	private suspend fun computeChildren(todo: Todo): Float {
		val children = getTodoChildrenUseCase(todo)
		val successChildren = children.getOrDefault(listOf())
		if (successChildren.isEmpty())
			return 0f

		var result = 0f
		var count = 0
		for (child in successChildren) {
			result += invoke(child)
			count++
		}

		return result / count
	}

}
