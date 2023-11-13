package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult

interface TaskRepository {

	suspend fun getTask(id: Int): RepositoryResult<BaseTask>

	suspend fun getChildren(fid: String): RepositoryResult<List<BaseTask>>

	suspend fun insert(task: BaseTask): RepositoryResult<BaseTask>

	suspend fun insert(list: List<BaseTask>): RepositoryResult<Unit>

	suspend fun replace(
		task: BaseTask,
		body: (BaseTask.() -> BaseTask)? = null
	): RepositoryResult<BaseTask>

	suspend fun replace(list: List<BaseTask>): RepositoryResult<Unit>

	suspend fun delete(task: BaseTask): Boolean

	suspend fun delete(list: List<BaseTask>): RepositoryResult<Unit>

	suspend fun deleteAll()

}