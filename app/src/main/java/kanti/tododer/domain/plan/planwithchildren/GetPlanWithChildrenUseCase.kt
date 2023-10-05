package kanti.tododer.domain.plan.planwithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.fullId
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.TaskRepository
import javax.inject.Inject

class GetPlanWithChildrenUseCase @Inject constructor(
	private val planRepository: PlanRepository,
	private val taskRepository: TaskRepository
) {

	suspend operator fun invoke(id: Int): RepositoryResult<PlanWithChildren> {
		val repositoryResult = planRepository.getPlan(id)

		val plan = repositoryResult.value
		val childPlans = getChildPlans(plan?.fullId)
		val childTasks = getChildTasks(plan?.fullId)

		val planWithChildren = PlanWithChildren(
			plan,
			childPlans,
			childTasks
		)
		return RepositoryResult(planWithChildren, repositoryResult.type)
	}

	private suspend fun getChildPlans(fid: String?): List<Plan> {
		if (fid == null)
			return listOf()
		val repositoryResult = planRepository.getChildren(fid)
		return repositoryResult.value ?: listOf()
	}

	private suspend fun getChildTasks(fid: String?): List<Task>  {
		if (fid == null)
			return listOf()
		val repositoryResult = taskRepository.getChildren(fid)
		return repositoryResult.value ?: listOf()
	}

}