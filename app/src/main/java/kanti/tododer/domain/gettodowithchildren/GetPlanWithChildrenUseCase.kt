package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asSuccess
import kanti.tododer.data.model.common.result.toAnotherGenericType
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetPlanChildrenUseCase
import javax.inject.Inject

class GetPlanWithChildrenUseCase @Inject constructor(
	private val planRepository: PlanRepository,
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase
) {

	suspend operator fun invoke(id: Int): GetRepositoryResult<TodoWithChildren> {
		val repositoryResult = planRepository.getPlan(id)
		val successResult = repositoryResult.asSuccess
			?: return repositoryResult.toAnotherGenericType()

		val childPlans = getPlanChildrenUseCase(successResult.value)
		if (childPlans.isFailure) {
			val th = childPlans.exceptionOrNull()
			return GetRepositoryResult.Fail(th?.message, th)
		}

		val planWithChildren = TodoWithChildren(
			successResult.value,
			childPlans.getOrDefault(listOf())
		)
		return GetRepositoryResult.Success(planWithChildren)
	}

}