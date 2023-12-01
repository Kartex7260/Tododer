package kanti.tododer.data.progress.datasource

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.progress.TodoProgress
import javax.inject.Inject

class TodoProgressRoomDataSource @Inject constructor(
	private val planProgressDao: IPlanProgressDao
) : ITodoProgressLocalDataSource {

	override suspend fun getPlanProgress(fullId: String): LocalResult<TodoProgress> {
		val planProgress = planProgressDao.getPlanProgress(fullId)
			?: return LocalResult(type = LocalResult.Type.NotFound(fullId))
		return LocalResult(planProgress.asTodoProgress)
	}

	override suspend fun insert(todoProgress: TodoProgress): LocalResult<TodoProgress> {
		val rowId = planProgressDao.insert(todoProgress)
		val planProgressFromDB = planProgressDao.getPlanProgressByRowId(rowId)
			?: return LocalResult(type = LocalResult.Type.Fail())
		return LocalResult(planProgressFromDB.asTodoProgress)
	}

	override suspend fun delete(todoProgress: TodoProgress) {
		planProgressDao.delete(todoProgress)
	}
}