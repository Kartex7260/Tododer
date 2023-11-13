package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult

interface TaskRepository {

	suspend fun getTask(id: Int): RepositoryResult<BaseTask>

	suspend fun getChildren(fid: String): RepositoryResult<List<BaseTask>>

	suspend fun insert(vararg task: BaseTask): RepositoryResult<Unit>

	suspend fun insert(task: BaseTask): RepositoryResult<BaseTask>

	suspend fun update(vararg task: BaseTask): RepositoryResult<Unit>

	suspend fun update(
		task: BaseTask,
		update: (BaseTask.() -> BaseTask)? = null
	): RepositoryResult<BaseTask>

	suspend fun delete(vararg task: BaseTask): RepositoryResult<Unit>

	suspend fun delete(task: BaseTask): Boolean

	suspend fun deleteAll()

}