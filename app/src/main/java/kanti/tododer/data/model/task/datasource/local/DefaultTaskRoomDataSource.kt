package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.toTask

class DefaultTaskRoomDataSource(
	private val taskDao: BaseTaskDao
) : TaskLocalDataSource {

	override suspend fun getTask(id: Int): LocalResult<BaseTask> {
		return localTryCatch {
			val taskEntity = taskDao.getTask(id)
				?: return@localTryCatch LocalResult(type = LocalResult.Type.NotFound())
			LocalResult(taskEntity.toTask())
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<BaseTask>> {
		return localTryCatch {
			val children = taskDao.getChildren(fid).map { it.toTask() }
			LocalResult(children)
		}
	}

	override suspend fun insert(task: BaseTask): LocalResult<BaseTask> {
		return localTryCatch {
			val rowId = taskDao.insert(task)
			if (rowId == -1L) {
				LocalResult(type = LocalResult.Type.AlreadyExists(task.fullId))
			} else {
				val taskFromDB = taskDao.getByRowId(rowId)?.toTask()
				LocalResult(taskFromDB)
			}
		}
	}

	override suspend fun insert(task: List<BaseTask>): LocalResult<Unit> {
		return localTryCatch {
			taskDao.insert(task)
			LocalResult()
		}
	}

	override suspend fun replace(task: BaseTask): LocalResult<BaseTask> {
		return localTryCatch {
			val rowId = taskDao.replace(task)
			val taskFromDB = taskDao.getByRowId(rowId)?.toTask()
			LocalResult(taskFromDB)
		}
	}

	override suspend fun replace(task: List<BaseTask>): LocalResult<Unit> {
		return localTryCatch {
			taskDao.replace(task)
			LocalResult()
		}
	}

	override suspend fun delete(task: BaseTask): Boolean {
		return try {
			taskDao.delete(task) == 1
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun delete(task: List<BaseTask>): LocalResult<Unit> {
		return localTryCatch {
			taskDao.delete(task)
			LocalResult()
		}
	}

	override suspend fun deleteAll() = taskDao.deleteAll()

}