package kanti.tododer.domain.gettodowithchildren

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.di.StandardDataQualifier
import kanti.tododer.domain.common.TodoWithChildren
import kanti.tododer.domain.gettodochildren.GetPlanChildrenUseCase
import kanti.tododer.domain.todomove.RepositorySet
import javax.inject.Inject

class GetPlanWithChildrenUseCase @Inject constructor(
	private val getPlanChildrenUseCase: GetPlanChildrenUseCase
) {

	suspend operator fun invoke(
		repositorySet: RepositorySet,
		id: Int
	): RepositoryResult<TodoWithChildren> {
		val repositoryResult = repositorySet.planRepository.getPlan(id)

		val plan = repositoryResult.value
		val childPlans = if (plan != null)
			getPlanChildrenUseCase(
				repositorySet,
				plan.toFullId
			)
		else
			listOf()

		val planWithChildren = TodoWithChildren(
			plan,
			childPlans
		)
		return RepositoryResult(planWithChildren, repositoryResult.type)
	}

}