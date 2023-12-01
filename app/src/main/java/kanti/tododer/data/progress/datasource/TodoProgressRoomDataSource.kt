package kanti.tododer.data.progress.datasource

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.common.resultTryCatch
import kanti.tododer.data.progress.TodoProgress
import kanti.tododer.data.progress.toTodoProgress
import javax.inject.Inject

class TodoProgressRoomDataSource @Inject constructor(
	private val planProgressDao: TodoProgressDao
) : TodoProgressLocalDataSource {

	override suspend fun getPlanProgress(fullId: String): GetLocalResult<TodoProgress> {
		val progress = planProgressDao.getPlanProgress(fullId)?.toTodoProgress()
			?: return GetLocalResult.NotFound(fullId)
		return GetLocalResult.Success(progress)
	}

	override suspend fun insert(todoProgress: TodoProgress): Result<TodoProgress> {
		val rowId = planProgressDao.insert(todoProgress.toTodoProgressEntity())[0]
		val planProgressFromDB = planProgressDao.getPlanProgressByRowId(rowId)?.toTodoProgress() ?:
		return Result.failure(IllegalArgumentException(
			"Unexpected error: not found by rowId: TodoProgress=$todoProgress, rowId=$rowId"
		))
		return Result.success(planProgressFromDB)
	}

	override suspend fun insert(vararg todoProgress: TodoProgress): Result<Unit> {
		return resultTryCatch {
			planProgressDao.insert(*todoProgress.map { it.toTodoProgressEntity() }.toTypedArray())
		}
	}

	override suspend fun delete(vararg todoProgress: TodoProgress): Result<Unit> {
		return resultTryCatch {
			planProgressDao.delete(*todoProgress.map { it.toTodoProgressEntity() }.toTypedArray())
		}
	}
}