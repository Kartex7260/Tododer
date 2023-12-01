package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.common.toFullId
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetPlanChildrenUseCase
import javax.inject.Inject

class GetPlanWithChildrenUseCase @Inject constructor(
	private val planRepository: IPlanRepository,
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase
) {

	suspend operator fun invoke(id: Int): RepositoryResult<TodoWithChildren> {
		val repositoryResult = planRepository.getPlan(id)

		val plan = repositoryResult.value
		val childPlans = if (plan != null)
			getPlanChildrenUseCase(plan.toFullId)
		else
			listOf()

		val planWithChildren = TodoWithChildren(
			plan,
			childPlans
		)
		return RepositoryResult(planWithChildren, repositoryResult.type)
	}

}