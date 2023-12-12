package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.common.resultTryCatch
import kanti.tododer.data.common.todoLocalResultTryCatch
import kanti.tododer.data.common.todoResultTryCatch
import kanti.tododer.data.model.common.localUnexpectedByRowId
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.toPlan
import javax.inject.Inject

class PlanRoomDataSourceImpl @Inject constructor(
	private val planDao: PlanDao
) : PlanLocalDataSource {

	override suspend fun getPlan(id: Int): GetLocalResult<Plan> {
		return todoLocalResultTryCatch {
			val plan = planDao.getPlan(id)?.toPlan()
				?: return@todoLocalResultTryCatch GetLocalResult.NotFound(id.toString())
			GetLocalResult.Success(plan)
		}
	}

	override suspend fun getChildren(fid: String): Result<List<Plan>> {
		return todoResultTryCatch {
			val children = planDao.getChildren(fid).map { it.toPlan() }
			Result.success(children)
		}
	}

	override suspend fun insert(plan: Plan): Result<Plan> {
		return todoResultTryCatch {
			val rowId = planDao.insert(plan.toPlanEntity())[0]
			val planFromDB = planDao.getByRowId(rowId)?.toPlan() ?:
				return@todoResultTryCatch localUnexpectedByRowId(plan, rowId)
			Result.success(planFromDB)
		}
	}

	override suspend fun insert(vararg plan: Plan): Result<Unit> {
		return resultTryCatch {
			planDao.insert(*plan.map { it.toPlanEntity() }.toTypedArray())
		}
	}

	override suspend fun delete(vararg plan: Plan): Result<Unit> {
		return resultTryCatch {
			planDao.delete(*plan.map { it.toPlanEntity() }.toTypedArray())
		}
	}

	override suspend fun deleteAll(): Result<Unit> {
		return resultTryCatch {
			planDao.deleteAll()
		}
	}

}