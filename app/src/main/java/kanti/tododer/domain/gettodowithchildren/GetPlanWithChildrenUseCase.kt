package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetPlanChildrenUseCase
import javax.inject.Inject

class GetPlanWithChildrenUseCase @Inject constructor(
	@StandardDataQualifier private val planRepository: PlanRepository,
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