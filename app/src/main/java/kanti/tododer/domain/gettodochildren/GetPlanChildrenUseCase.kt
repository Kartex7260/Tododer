package kanti.tododer.domain.gettodochildren

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetPlanChildrenUseCase @Inject constructor() {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		todo: Todo
	): List<Todo> {
		val childrenPlan = getChildrenPlans(repositorySet.planRepository, todo)
		val childrenTask = getChildrenTasks(repositorySet.taskRepository, todo)
		return childrenPlan + childrenTask
	}

	private suspend fun getChildrenPlans(
		planRepository: PlanRepository,
		todo: Todo
	): List<BasePlan> {
		return planRepository.getChildren(todo.fullId).value ?: listOf()
	}

	private suspend fun getChildrenTasks(
		taskRepository: TaskRepository,
		todo: Todo
	): List<BaseTask> {
		return taskRepository.getChildren(todo.fullId).value ?: listOf()
	}

}
