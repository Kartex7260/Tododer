package kanti.tododer.data.model.task.archive

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.datasource.local.ITaskLocalDataSource
import kanti.tododer.di.ArchiveDataQualifier
import javax.inject.Inject

class ArchiveTaskRepository @Inject constructor(
	@ArchiveDataQualifier private val archiveTaskLocal: ITaskLocalDataSource
) : ITaskRepository {

	override suspend fun getTask(id: Int): RepositoryResult<Task> {
		return archiveTaskLocal.getTask(id).toRepositoryResult()
	}

	override suspend fun getChildren(fid: String): RepositoryResult<List<Task>> {
		return archiveTaskLocal.getChildren(fid).toRepositoryResult()
	}

	override suspend fun insert(task: Task): RepositoryResult<Task> {
		return archiveTaskLocal.insert(task).toRepositoryResult()
	}

	override suspend fun replace(task: Task, body: (Task.() -> Task)?): RepositoryResult<Task> {
		return if (body == null) {
			archiveTaskLocal.replace(task)
		} else {
			archiveTaskLocal.replace(task.body())
		}.toRepositoryResult()
	}

	override suspend fun delete(task: Task): Boolean {
		return archiveTaskLocal.delete(task)
	}

	override suspend fun deleteAll() {
		archiveTaskLocal.deleteAll()
	}
}