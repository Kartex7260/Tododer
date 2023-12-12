package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import javax.inject.Inject

class GetPlanChildrenUseCase @Inject constructor(
	private val taskRepository: TaskRepository,
	private val planRepository: PlanRepository
) {

	suspend operator fun invoke(todo: Todo): Result<List<Todo>> {
		val childrenPlanResult = planRepository.getChildren(todo.fullId)
		val childrenTaskResult = taskRepository.getChildren(todo.fullId)
		if (childrenPlanResult.isFailure)
			return childrenPlanResult
		if (childrenTaskResult.isFailure)
			return childrenTaskResult
		return Result.success(
			value = childrenPlanResult.getOrDefault(listOf())
					+ childrenTaskResult.getOrDefault(listOf())
		)
	}

}
