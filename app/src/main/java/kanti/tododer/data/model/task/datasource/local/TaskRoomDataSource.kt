package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.toTask
import javax.inject.Inject

class TaskRoomDataSource @Inject constructor(
	private val taskDao: TaskDao
) : ITaskLocalDataSource {

	override suspend fun getTask(id: Int): LocalResult<Task> {
		return localTryCatch {
			val taskEntity = taskDao.getTask(id)
				?: return@localTryCatch LocalResult(type = LocalResult.Type.NotFound())
			LocalResult(taskEntity.toTask())
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Task>> {
		return localTryCatch {
			val children = taskDao.getChildren(fid).map { it.toTask() }
			LocalResult(children)
		}
	}

	override suspend fun insert(task: Task): LocalResult<Task> {
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

	override suspend fun replace(task: Task): LocalResult<Task> {
		return localTryCatch {
			val rowId = taskDao.replace(task)
			val taskFromDB = taskDao.getByRowId(rowId)?.toTask()
			LocalResult(taskFromDB)
		}
	}

	override suspend fun delete(task: Task): Boolean {
		return try {
			taskDao.delete(task) == 1
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun deleteAll() = taskDao.deleteAll()

}