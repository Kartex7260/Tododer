package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.plan.Plan

class DefaultPlanRoomDataSource(
	private val planDao: BasePlanDao
) : PlanLocalDataSource {

	override suspend fun getPlan(id: Int): LocalResult<Plan> {
		return localTryCatch {
			val planFromDS = planDao.getPlan(id)
			LocalResult(
				value = planFromDS,
				type = if (planFromDS == null) {
					LocalResult.Type.NotFound(id.toString())
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Plan>> {
		return localTryCatch {
			val children = planDao.getChildren(fid)
			LocalResult(
				value = children
			)
		}
	}

	override suspend fun insert(vararg plan: Plan): LocalResult<Unit> {
		return localTryCatch {
			planDao.insert(*plan)
			LocalResult()
		}
	}

	override suspend fun insert(plan: Plan): LocalResult<Plan> {
		return localTryCatch {
			val planRowId = planDao.insert(plan)
			if (planRowId == -1L) {
				return@localTryCatch LocalResult(
					type = LocalResult.Type.AlreadyExists(plan.fullId)
				)
			}
			val planFromDB = planDao.getByRowId(planRowId)!!
			LocalResult(planFromDB)
		}
	}

	override suspend fun update(vararg plan: Plan): LocalResult<Unit> {
		return localTryCatch {
			planDao.update(*plan)
			LocalResult()
		}
	}

	override suspend fun update(plan: Plan): LocalResult<Plan> {
		return localTryCatch {
			planDao.update(plan)
			val planFromDB = planDao.getPlan(plan.id)!!
			LocalResult(planFromDB)
		}
	}

	override suspend fun delete(vararg plan: Plan): LocalResult<Unit> {
		return localTryCatch {
			planDao.delete(*plan)
			LocalResult()
		}
	}

	override suspend fun delete(plan: Plan): Boolean {
		return try {
			planDao.delete(plan)
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun deleteAll() {
		planDao.deleteAll()
	}

}