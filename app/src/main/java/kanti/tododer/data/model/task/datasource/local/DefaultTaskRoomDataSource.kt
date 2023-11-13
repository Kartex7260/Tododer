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

	override suspend fun insert(vararg task: BaseTask): LocalResult<Unit> {
		return localTryCatch {
			taskDao.insert(*task)
			LocalResult()
		}
	}

	override suspend fun insert(task: BaseTask): LocalResult<BaseTask> {
		return localTryCatch {
			val taskRowId = taskDao.insert(task)
			if (taskRowId == -1L)
				return@localTryCatch LocalResult(
					type = LocalResult.Type.AlreadyExists(task.fullId)
				)
			val taskFromDB = taskDao.getByRowId(taskRowId)!!
			LocalResult(taskFromDB)
		}
	}

	override suspend fun update(vararg task: BaseTask): LocalResult<Unit> {
		return localTryCatch {
			taskDao.update(*task)
			LocalResult()
		}
	}

	override suspend fun update(task: BaseTask): LocalResult<BaseTask> {
		return localTryCatch {
			taskDao.update(task)
			val taskFromDB = taskDao.getTask(task.id)!!
			LocalResult(taskFromDB)
		}
	}

	override suspend fun delete(vararg task: BaseTask): LocalResult<Unit> {
		return localTryCatch {
			taskDao.delete(*task)
			LocalResult()
		}
	}

	override suspend fun delete(task: BaseTask): Boolean {
		return try {
			taskDao.delete(task)
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun deleteAll() {
		try {
			taskDao.deleteAll()
		} catch (_: Throwable) {
		}
	}

}