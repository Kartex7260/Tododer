package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource

class DefaultTaskRepositoryImpl(
	private val localDataSource: TaskLocalDataSource
) : TaskRepository {

	override suspend fun getTask(id: Int): RepositoryResult<BaseTask> {
		val task = localDataSource.getTask(id)
		return task.toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<BaseTask>> {
		return localDataSource.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(vararg task: BaseTask): RepositoryResult<Unit> {
		return localDataSource.insert(*task).toRepositoryResult()
	}

	override suspend fun insert(task: BaseTask): RepositoryResult<BaseTask> {
		return localDataSource.insert(task).toRepositoryResult()
	}

	override suspend fun update(vararg task: BaseTask): RepositoryResult<Unit> {
		return localDataSource.update(*task).toRepositoryResult()
	}

	override suspend fun update(
		task: BaseTask,
		update: (BaseTask.() -> BaseTask)?
	): RepositoryResult<BaseTask> {
		val updatedTask = update?.let { task.it() } ?: task
		return localDataSource.update(updatedTask).toRepositoryResult()
	}

	override suspend fun delete(vararg task: BaseTask): RepositoryResult<Unit> {
		return localDataSource.delete(*task).toRepositoryResult()
	}

	override suspend fun delete(task: BaseTask): Boolean {
		return localDataSource.delete(task)
	}

	override suspend fun deleteAll() = localDataSource.deleteAll()

}