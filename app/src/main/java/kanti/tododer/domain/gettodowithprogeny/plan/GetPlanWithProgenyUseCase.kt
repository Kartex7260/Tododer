package kanti.tododer.domain.gettodowithprogeny.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.TaskRepository

class GetPlanWithProgenyUseCase constructor(
	private val taskRepository: TaskRepository,
	private val planRepository: PlanRepository
) {

	suspend operator fun invoke(todo: Todo): RepositoryResult<PlanWithProgeny> {
		val plan = planRepository.getPlan(todo.id).value
			?: return RepositoryResult(type = RepositoryResult.Type.NotFound(todo.fullId))

		val plans = mutableListOf<BasePlan>()
		val tasks = mutableListOf<BaseTask>()
		plans.add(plan)

		getPlansToList(plan, plans, tasks)
		return RepositoryResult(
			value = PlanWithProgeny(
				plans = plans,
				tasks = tasks
			)
		)
	}

	private suspend fun getPlansToList(
		todo: Todo,
		planList: MutableList<BasePlan>,
		taskList: MutableList<BaseTask>
	) {
		getTasksToList(todo, taskList)
		val plansChildren = planRepository.getChildren(todo.fullId).value ?: return

		for (planChild in plansChildren) {
			planList.add(planChild)
			getPlansToList(planChild, planList, taskList)
		}
	}

	private suspend fun getTasksToList(todo: Todo, list: MutableList<BaseTask>) {
		val children = taskRepository.getChildren(todo.fullId).value ?: return

		for (child in children) {
			list.add(child)
			getTasksToList(child, list)
		}
	}

}