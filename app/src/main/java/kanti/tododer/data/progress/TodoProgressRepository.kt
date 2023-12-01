package kanti.tododer.data.progress

import kanti.tododer.data.model.common.result.GetRepositoryResult

interface TodoProgressRepository {

	suspend fun getPlanProgress(fullId: String): GetRepositoryResult<TodoProgress>

	suspend fun insert(todoProgress: TodoProgress): Result<TodoProgress>

	suspend fun insert(vararg todoProgress: TodoProgress): Result<Unit>

	suspend fun delete(vararg todoProgress: TodoProgress): Result<Unit>

}