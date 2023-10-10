package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.tryCatch
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.fullId
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	private val planDao: IPlanDao
) : IPlanLocalDataSource {

	override suspend fun getPlan(id: Int): LocalResult<Plan> {
		return tryCatch {
			val plan = planDao.getPlan(id)?.toPlan()
				?: return@tryCatch LocalResult(type = LocalResult.Type.NotFound())
			LocalResult(plan)
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Plan>> {
		return tryCatch {
			val children = planDao.getChildren(fid).map { it.toPlan() }
			LocalResult(children)
		}
	}

	override suspend fun insert(plan: Plan): LocalResult<Plan> {
		return tryCatch {
			val rowId = planDao.insert(plan)
			if (rowId == -1L) {
				return@tryCatch LocalResult(type = LocalResult.Type.AlreadyExists(plan.fullId))
			}
			val planFromDB = planDao.getByRowId(rowId).toPlan()
			LocalResult(planFromDB)
		}
	}

	override suspend fun replace(plan: Plan): LocalResult<Plan> {
		return tryCatch {
			val rowId = planDao.replace(plan)
			val planFromDB = planDao.getByRowId(rowId).toPlan()
			LocalResult(planFromDB)
		}
	}

	override suspend fun delete(plan: Plan): Boolean {
		return try {
			planDao.delete(plan) == 1
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun deleteAll() {
		return try {
			planDao.deleteAll()
		} catch (_: Throwable) {
		}
	}

}