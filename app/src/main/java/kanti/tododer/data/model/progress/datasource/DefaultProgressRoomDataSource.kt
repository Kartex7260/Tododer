package kanti.tododer.data.model.progress.datasource

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.progress.BaseTodoProgress

class DefaultProgressRoomDataSource(
	private val todoProgressDao: BaseTodoProgressDao
) : TodoProgressLocalDataSource {

	override suspend fun getTodoProgress(fullId: String): LocalResult<BaseTodoProgress> {
		return localTryCatch {
			val todoProgressFromDB = todoProgressDao.getTodoProgress(fullId)
			LocalResult(
				value = todoProgressFromDB,
				type = if (todoProgressFromDB == null) {
					LocalResult.Type.NotFound(fullId)
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun insert(todoProgress: BaseTodoProgress): LocalResult<BaseTodoProgress> {
		return localTryCatch {
			val todoProgressRowId = todoProgressDao.insert(todoProgress)
			val todoProgressFromDB = todoProgressDao.getTodoProgressByRowId(todoProgressRowId)
			LocalResult(
				value = todoProgressFromDB,
				type = if (todoProgressFromDB == null) {
					LocalResult.Type.Fail("Unexpected error, replacing = ${todoProgress.fullId}")
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun delete(todoProgress: BaseTodoProgress) {
		todoProgressDao.delete(todoProgress)
	}
}