package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.plan.BasePlan

class DefaultPlanRoomDataSource(
	private val localData: BasePlanDao
) : PlanLocalDataSource {

	override suspend fun getPlan(id: Int): LocalResult<BasePlan> {
		return localTryCatch {
			val planFromDS = localData.getPlan(id)
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

	override suspend fun getChildren(fid: String): LocalResult<List<BasePlan>> {
		return localTryCatch {
			val children = localData.getChildren(fid)
			LocalResult(
				value = children
			)
		}
	}

	override suspend fun insert(plan: BasePlan): LocalResult<BasePlan> {
		return localTryCatch {
			val planRowId = localData.insert(plan)
			val planFromDS = localData.getByRowId(planRowId)
			LocalResult(
				value = planFromDS,
				type = if (planFromDS == null) {
					LocalResult.Type.AlreadyExists(plan.fullId)
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun replace(plan: BasePlan): LocalResult<BasePlan> {
		return localTryCatch {
			val planRowId = localData.replace(plan)
			val planFromDS = localData.getByRowId(planRowId)
			LocalResult(
				value = planFromDS,
				type = if (planFromDS == null) {
					LocalResult.Type.Fail(plan.fullId)
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun delete(plan: BasePlan): Boolean {
		return localData.delete(plan) == 1
	}

	override suspend fun deleteAll() {
		localData.deleteAll()
	}

}