package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import javax.inject.Inject

class PlanRepository @Inject constructor(
	private val planLocal: PlanLocalDataSource
) {

	suspend fun getPlan(id: Int): RepositoryResult<Plan> {
		val plan = planLocal.getPlan(id)
		return plan.toRepositoryResult()
	}

	suspend fun getChildren(fid: String): RepositoryResult<List<Plan>> = planLocal
		.getChildren(fid).toRepositoryResult()

	suspend fun insert(plan: Plan): Plan = planLocal.insert(plan)

	suspend fun replace(plan: Plan, body: (Plan.() -> Plan)? = null): Plan {
		val newPlan = body?.let { plan.it() }
		return planLocal.replace(newPlan ?: plan)
	}

	suspend fun delete(plan: Plan): Boolean = planLocal.delete(plan)

}