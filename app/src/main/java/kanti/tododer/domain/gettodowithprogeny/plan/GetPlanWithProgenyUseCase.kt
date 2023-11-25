package kanti.tododer.domain.gettodowithprogeny.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.Task
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetPlanWithProgenyUseCase @Inject constructor() {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		todo: Todo
	): RepositoryResult<PlanWithProgeny> {
		val plan = repositorySet.planRepository.getTodo(todo.id).value
			?: return RepositoryResult(type = RepositoryResult.Type.NotFound(todo.fullId))

		val plans = mutableListOf<Plan>()
		val tasks = mutableListOf<Task>()
		plans.add(plan)

		getPlansToList(repositorySet, plan, plans, tasks)
		return RepositoryResult(
			value = PlanWithProgeny(
				plans = plans,
				tasks = tasks
			)
		)
	}

	private suspend fun getPlansToList(
		repositorySet: RepositorySet,
		todo: Todo,
		planList: MutableList<Plan>,
		taskList: MutableList<Task>
	) {
		getTasksToList(repositorySet, todo, taskList)
		val plansChildren = repositorySet.planRepository.getChildren(todo.fullId).value ?: return

		for (planChild in plansChildren) {
			planList.add(planChild)
			getPlansToList(repositorySet, planChild, planList, taskList)
		}
	}

	private suspend fun getTasksToList(
		repositorySet: RepositorySet,
		todo: Todo,
		list: MutableList<Task>
	) {
		val children = repositorySet.taskRepository.getChildren(todo.fullId).value ?: return

		for (child in children) {
			list.add(child)
			getTasksToList(repositorySet, child, list)
		}
	}

}