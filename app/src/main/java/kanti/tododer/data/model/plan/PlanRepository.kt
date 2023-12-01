package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.IPlanLocalDataSource
import javax.inject.Inject

class PlanRepository @Inject constructor(
	private val planLocal: IPlanLocalDataSource
) : IPlanRepository {

	override suspend fun getPlan(id: Int): RepositoryResult<Plan> {
		val plan = planLocal.getPlan(id)
		return plan.toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<Plan>> = planLocal
		.getChildren(fid).toRepositoryResult()

	override suspend fun insert(plan: Plan): RepositoryResult<Plan> = planLocal.insert(plan)
		.toRepositoryResult()

	override suspend fun replace(plan: Plan, body: (Plan.() -> Plan)?): RepositoryResult<Plan> {
		val newPlan = body?.let { plan.it() }
		return planLocal.replace(newPlan ?: plan).toRepositoryResult()
	}

	override suspend fun delete(plan: Plan): Boolean = planLocal.delete(plan)

	override suspend fun deleteAll() = planLocal.deleteAll()

}