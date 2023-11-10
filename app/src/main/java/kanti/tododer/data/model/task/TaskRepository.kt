package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult

interface TaskRepository {

	suspend fun getTask(id: Int): RepositoryResult<BaseTask>

	suspend fun getChildren(fid: String): RepositoryResult<List<BaseTask>>

	suspend fun insert(task: BaseTask): RepositoryResult<BaseTask>

	suspend fun replace(
		task: BaseTask,
		body: (BaseTask.() -> BaseTask)? = null
	): RepositoryResult<BaseTask>

	suspend fun delete(task: BaseTask): Boolean

	suspend fun deleteAll()

}