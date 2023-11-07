package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.toPlan
import javax.inject.Inject

class PlanRoomDataSource @Inject constructor(
	private val planDao: IPlanDao
) : IPlanLocalDataSource {

	override suspend fun getPlan(id: Int): LocalResult<Plan> {
		return localTryCatch {
			val plan = planDao.getPlan(id)?.toPlan()
				?: return@localTryCatch LocalResult(type = LocalResult.Type.NotFound())
			LocalResult(plan)
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Plan>> {
		return localTryCatch {
			val children = planDao.getChildren(fid).map { it.toPlan() }
			LocalResult(children)
		}
	}

	override suspend fun insert(plan: Plan): LocalResult<Plan> {
		return localTryCatch {
			val rowId = planDao.insert(plan)
			if (rowId == -1L) {
				return@localTryCatch LocalResult(type = LocalResult.Type.AlreadyExists(plan.fullId))
			}
			val planFromDB = planDao.getByRowId(rowId)?.toPlan()
			LocalResult(
				value = planFromDB,
				type = if (planFromDB == null) {
					LocalResult.Type.NotFound(rowId.toString())
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun replace(plan: Plan): LocalResult<Plan> {
		return localTryCatch {
			val rowId = planDao.replace(plan)
			val planFromDB = planDao.getByRowId(rowId)?.toPlan()
			LocalResult(
				value = planFromDB,
				type = if (planFromDB == null) {
					LocalResult.Type.NotFound(rowId.toString())
				} else {
					LocalResult.Type.Success
				}
			)
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