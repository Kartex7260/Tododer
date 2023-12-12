package kanti.tododer.data.model.plan

import kanti.tododer.common.Const

suspend fun PlanRepository.getFromRoot(): Result<List<Plan>> {
	return getChildren(Const.ROOT_PARENT_ID)
}

suspend fun PlanRepository.insertToRoot(plan: Plan? = null): Result<Plan> {
	val rootPlan = plan?.toPlan(parentId = Const.ROOT_PARENT_ID)
		?: Plan(parentId = Const.ROOT_PARENT_ID)
	return insert(rootPlan)
}
