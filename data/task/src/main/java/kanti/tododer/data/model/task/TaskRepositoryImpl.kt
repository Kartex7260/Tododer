package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asRepositoryResult
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
	private val taskLocal: TaskLocalDataSource
) : TaskRepository {

	override suspend fun getTask(id: Int): GetRepositoryResult<Task> {
		return taskLocal.getTask(id).asRepositoryResult
	}

	override suspend fun getChildren(fid: String): Result<List<Task>> {
		return taskLocal.getChildren(fid)
	}

	override suspend fun insert(task: Task): Result<Task> {
		return taskLocal.insert(task)
	}

	override suspend fun insert(vararg task: Task): Result<Unit> {
		return taskLocal.insert(*task)
	}

	override suspend fun delete(vararg task: Task): Result<Unit> {
		return taskLocal.delete(*task)
	}

	override suspend fun deleteAll(): Result<Unit> {
		return taskLocal.deleteAll()
	}

}