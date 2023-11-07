package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.task.datasource.local.ITaskLocalDataSource
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class TaskRepository @Inject constructor(
	@StandardDataQualifier private val taskLocal: ITaskLocalDataSource
) : ITaskRepository {

	override suspend fun getTask(id: Int): RepositoryResult<Task> {
		val task = taskLocal.getTask(id)
		return task.toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<Task>> {
		return taskLocal.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(task: Task): RepositoryResult<Task> = taskLocal.insert(task)
		.toRepositoryResult()

	override suspend fun replace(task: Task, body: (Task.() -> Task)?): RepositoryResult<Task> {
		val newTask = body?.let { task.it() }
		val taskFromDB = taskLocal.replace(newTask ?: task)
		return taskFromDB.toRepositoryResult()
	}

	override suspend fun delete(task: Task): Boolean = taskLocal.delete(task)

	override suspend fun deleteAll() = taskLocal.deleteAll()

}