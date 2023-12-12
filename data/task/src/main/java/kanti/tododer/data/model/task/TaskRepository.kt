package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.result.GetRepositoryResult

interface TaskRepository {

	suspend fun getTask(id: Int): GetRepositoryResult<Task>

	suspend fun getChildren(fid: String): Result<List<Task>>

	suspend fun insert(task: Task): Result<Task>

	suspend fun insert(vararg task: Task): Result<Unit>

	suspend fun delete(vararg task: Task): Result<Unit>

	suspend fun deleteAll(): Result<Unit>

}