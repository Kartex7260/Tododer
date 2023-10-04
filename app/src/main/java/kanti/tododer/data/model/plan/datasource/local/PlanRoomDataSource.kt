package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.asPlanEntity
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	private val planDao: PlanDao
) : PlanLocalDataSource {

	override suspend fun getPlan(id: Int): LocalResult<Plan> {
		val plan = planDao.get(id) ?: return LocalResult(type = LocalResult.Type.NotFound())
		return LocalResult(plan.toPlan())
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Plan>> {
		val children = planDao.getChildren(fid).map { it.toPlan() }
		return LocalResult(children)
	}

	override suspend fun insert(plan: Plan): Plan {
		val rowId = planDao.insert(plan)
		return planDao.getByRowId(rowId).toPlan()
	}

	override suspend fun replace(plan: Plan): Plan {
		val rowId = planDao.replace(plan)
		return planDao.getByRowId(rowId).toPlan()
	}

	override suspend fun delete(plan: Plan): Boolean {
		return planDao.delete(plan) == 1
	}
}