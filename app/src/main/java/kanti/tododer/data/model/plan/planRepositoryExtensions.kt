package kanti.tododer.data.model.plan

import kanti.tododer.common.Const
import kanti.tododer.data.common.RepositoryResult

suspend fun PlanRepository.getFromRoot(): RepositoryResult<List<Plan>> {
	return getChildren(Const.ROOT_PARENT_ID)
}

suspend fun PlanRepository.insertToRoot(plan: Plan? = null): RepositoryResult<Plan> {
	val rootPlan = plan?.copy(parentId = Const.ROOT_PARENT_ID)
		?: Plan(parentId = Const.ROOT_PARENT_ID)
	return insert(rootPlan)
}
