package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.Task
import javax.inject.Inject

class TaskRoomDataSource @Inject constructor(
	private val taskDao: TaskDao
) : TaskLocalDataSource {

	override suspend fun getTask(id: Int): LocalResult<Task> {
		val taskEntity = taskDao.get(id) ?: return LocalResult(type = LocalResult.Type.NotFound())
		return LocalResult(taskEntity.toTask())
	}

	override suspend fun getChildren(fid: String): LocalResult<List<Task>> {
		val children = taskDao.getChildren(fid).map { it.toTask() }
		return LocalResult(children)
	}

	override suspend fun insert(task: Task): Task {
		val rowId = taskDao.insert(task)
		return taskDao.getByRowId(rowId).toTask()
	}

	override suspend fun replace(task: Task): Task {
		val rowId = taskDao.replace(task)
		return taskDao.getByRowId(rowId).toTask()
	}

	override suspend fun delete(task: Task): Boolean {
		return taskDao.delete(task) == 1
	}
}