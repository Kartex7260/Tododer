package kanti.tododer.data.model.task

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource

class DefaultTaskRepositoryImpl(
	private val taskLocal: TaskLocalDataSource
) : TaskRepository {

	override suspend fun getTask(id: Int): RepositoryResult<BaseTask> {
		val task = taskLocal.getTask(id)
		return task.toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<BaseTask>> {
		return taskLocal.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(task: BaseTask): RepositoryResult<BaseTask> {
		return taskLocal.insert(task).toRepositoryResult()
	}

	override suspend fun insert(list: List<BaseTask>): RepositoryResult<Unit> {
		return taskLocal.insert(list).toRepositoryResult()
	}

	override suspend fun replace(
		task: BaseTask,
		body: (BaseTask.() -> BaseTask)?
	): RepositoryResult<BaseTask> {
		val newTask = body?.let { task.it() }
		val taskFromDB = taskLocal.replace(newTask ?: task)
		return taskFromDB.toRepositoryResult()
	}

	override suspend fun replace(list: List<BaseTask>): RepositoryResult<Unit> {
		return taskLocal.replace(list).toRepositoryResult()
	}

	override suspend fun delete(task: BaseTask): Boolean = taskLocal.delete(task)

	override suspend fun delete(list: List<BaseTask>): RepositoryResult<Unit> {
		return taskLocal.delete(list).toRepositoryResult()
	}

	override suspend fun deleteAll() = taskLocal.deleteAll()

}