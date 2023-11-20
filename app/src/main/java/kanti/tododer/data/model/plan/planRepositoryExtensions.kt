package kanti.tododer.data.model.plan

import kanti.tododer.common.Const
import kanti.tododer.data.common.RepositoryResult

suspend fun PlanRepository.getFromRoot(): RepositoryResult<List<Plan>> {
	return getChildren(Const.ROOT_PARENT_ID)
}

suspend fun PlanRepository.insertToRoot(plan: Plan? = null): RepositoryResult<Plan> {
	val rootPlanImpl = plan?.toPlan(parentId = Const.ROOT_PARENT_ID)
		?: PlanImpl(parentId = Const.ROOT_PARENT_ID)
	return insert(rootPlanImpl)
}
