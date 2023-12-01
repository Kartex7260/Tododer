package kanti.tododer.data.model.plan

import kanti.tododer.common.Const
import kanti.tododer.data.model.common.result.GetRepositoryResult

suspend fun PlanRepository.getFromRoot(): Result<List<Plan>> {
	return getChildren(Const.ROOT_PARENT_ID)
}

suspend fun PlanRepository.insertToRoot(plan: Plan? = null): Result<Plan> {
	val rootPlan = plan?.toPlan(parentId = Const.ROOT_PARENT_ID)
		?: Plan(parentId = Const.ROOT_PARENT_ID)
	return insert(rootPlan)
}
