package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.toPlan

class DefaultRoomDataSource(
	private val localData: BasePlanDao
) : PlanLocalDataSource {

	override suspend fun getPlan(id: Int): LocalResult<Plan> {
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

	override suspend fun getChildren(fid: String): LocalResult<List<Plan>> {
		return localTryCatch {
			val children = localData.getChildren(fid)
			LocalResult(
				value = children
			)
		}
	}

	override suspend fun insert(plan: Plan): LocalResult<Plan> {
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

	override suspend fun replace(plan: Plan): LocalResult<Plan> {
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

	override suspend fun delete(plan: Plan): Boolean {
		return localData.delete(plan) == 1
	}

	override suspend fun deleteAll() {
		localData.deleteAll()
	}

}