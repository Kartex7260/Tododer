package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class GetPlanChildrenUseCase @Inject constructor(
	@StandardDataQualifier private val taskRepository: TaskRepository,
	private val planRepository: PlanRepository
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
