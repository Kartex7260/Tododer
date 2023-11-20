package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult

interface TaskRepository {

	suspend fun getTask(id: Int): RepositoryResult<Task>

	suspend fun getChildren(fid: String): RepositoryResult<List<Task>>

	suspend fun insert(vararg task: Task): RepositoryResult<Unit>

	suspend fun insert(task: Task): RepositoryResult<Task>

	suspend fun update(vararg task: Task): RepositoryResult<Unit>

	suspend fun update(
		task: Task,
		update: (Task.() -> Task)? = null
	): RepositoryResult<Task>

	suspend fun delete(vararg task: Task): RepositoryResult<Unit>

	suspend fun delete(task: Task): Boolean

	suspend fun deleteAll()

}