package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult

interface ITaskRepository {

	suspend fun getTask(id: Int): RepositoryResult<Task>

	suspend fun getChildren(fid: String): RepositoryResult<List<Task>>

	suspend fun insert(task: Task): RepositoryResult<Task>

	suspend fun replace(task: Task, body: (Task.() -> Task)? = null): RepositoryResult<Task>

	suspend fun delete(task: Task): Boolean

	suspend fun deleteAll()

}