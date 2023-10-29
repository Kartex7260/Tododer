package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import javax.inject.Inject

class GetPlanChildrenUseCase @Inject constructor(
	private val taskRepository: ITaskRepository,
	private val planRepository: IPlanRepository
) {

	suspend operator fun invoke(todo: Todo): List<Todo> {
		val childrenPlan = getChildrenPlans(todo)
		val childrenTask = getChildrenTasks(todo)
		return childrenPlan + childrenTask
	}

	private suspend fun getChildrenPlans(todo: Todo): List<Plan> {
		return planRepository.getChildren(todo.fullId).value ?: listOf()
	}

	private suspend fun getChildrenTasks(todo: Todo): List<Task> {
		return taskRepository.getChildren(todo.fullId).value ?: listOf()
	}

}
