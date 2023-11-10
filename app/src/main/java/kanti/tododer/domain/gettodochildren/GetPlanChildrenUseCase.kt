package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class GetPlanChildrenUseCase @Inject constructor(
	@StandardDataQualifier private val taskRepository: TaskRepository,
	@StandardDataQualifier private val planRepository: PlanRepository
) {

	suspend operator fun invoke(todo: Todo): List<Todo> {
		val childrenPlan = getChildrenPlans(todo)
		val childrenTask = getChildrenTasks(todo)
		return childrenPlan + childrenTask
	}

	private suspend fun getChildrenPlans(todo: Todo): List<BasePlan> {
		return planRepository.getChildren(todo.fullId).value ?: listOf()
	}

	private suspend fun getChildrenTasks(todo: Todo): List<BaseTask> {
		return taskRepository.getChildren(todo.fullId).value ?: listOf()
	}

}
