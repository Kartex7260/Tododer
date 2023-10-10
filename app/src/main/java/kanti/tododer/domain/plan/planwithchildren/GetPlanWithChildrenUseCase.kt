package kanti.tododer.domain.plan.planwithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.fullId
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import javax.inject.Inject

class GetPlanWithChildrenUseCase @Inject constructor(
	private val planRepository: IPlanRepository,
	private val taskRepository: ITaskRepository
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