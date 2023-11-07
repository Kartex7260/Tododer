package kanti.tododer.data.model.task.archive.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.common.fullId
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.datasource.local.ITaskLocalDataSource
import kanti.tododer.data.model.task.toTask
import javax.inject.Inject

class ArchiveTaskRoomDataSource @Inject constructor(
	private val archiveTaskDao: ArchiveTaskDao
) : ITaskLocalDataSource {

	override suspend fun getTask(id: Int): LocalResult<Task> {
		return localTryCatch {
			val task = archiveTaskDao.getTask(id)
			LocalResult(
				value = task?.toTask(),
				type = if (task == null) {
					LocalResult.Type.NotFound(id.toString())
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Task>> {
		return localTryCatch {
			val children = archiveTaskDao.getChildren(fid)
			LocalResult(children.map { it.toTask() })
		}
	}

	override suspend fun insert(task: Task): LocalResult<Task> {
		return localTryCatch {
			val taskRowId = archiveTaskDao.insert(task.toArchiveTaskEntity())
			val taskFromDB = archiveTaskDao.getByRowId(taskRowId)
			LocalResult(
				value = taskFromDB?.toTask(),
				type = if (taskFromDB == null) {
					LocalResult.Type.AlreadyExists(task.fullId)
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun replace(task: Task): LocalResult<Task> {
		return localTryCatch {
			val taskRowId = archiveTaskDao.replace(task.toArchiveTaskEntity())
			val taskFromDB = archiveTaskDao.getByRowId(taskRowId)
			LocalResult(
				value = taskFromDB?.toTask(),
				type = if (taskFromDB == null) {
					LocalResult.Type.Fail("Task $task no replaced")
				} else {
					LocalResult.Type.Success
				}
			)
		}
	}

	override suspend fun delete(task: Task): Boolean {
		return try {
			archiveTaskDao.delete(task.toArchiveTaskEntity()) == 1
		} catch (th: Throwable) {
			false
		}
	}

	override suspend fun deleteAll() {
		archiveTaskDao.deleteAll()
	}
}