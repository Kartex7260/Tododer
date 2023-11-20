package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.task.Task

class DefaultTaskRoomDataSource(
	private val taskDao: BaseTaskDao
) : TaskLocalDataSource {

	override suspend fun getTask(id: Int): LocalResult<Task> {
		return localTryCatch {
			val task = taskDao.getTask(id)
				?: return@localTryCatch LocalResult(type = LocalResult.Type.NotFound())
			LocalResult(task)
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Task>> {
		return localTryCatch {
			val children = taskDao.getChildren(fid)
			LocalResult(
				value = children
			)
		}
	}

	override suspend fun insert(vararg task: Task): LocalResult<Unit> {
		return localTryCatch {
			taskDao.insert(*task)
			LocalResult()
		}
	}

	override suspend fun insert(task: Task): LocalResult<Task> {
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

	override suspend fun update(vararg task: Task): LocalResult<Unit> {
		return localTryCatch {
			taskDao.update(*task)
			LocalResult()
		}
	}

	override suspend fun update(task: Task): LocalResult<Task> {
		return localTryCatch {
			taskDao.update(task)
			val taskFromDB = taskDao.getTask(task.id)!!
			LocalResult(taskFromDB)
		}
	}

	override suspend fun delete(vararg task: Task): LocalResult<Unit> {
		return localTryCatch {
			taskDao.delete(*task)
			LocalResult()
		}
	}

	override suspend fun delete(task: Task): Boolean {
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