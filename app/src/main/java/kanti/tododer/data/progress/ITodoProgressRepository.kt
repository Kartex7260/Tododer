package kanti.tododer.data.progress

import kanti.tododer.data.common.RepositoryResult

interface ITodoProgressRepository {

	suspend fun getPlanProgress(fullId: String): RepositoryResult<TodoProgress>

	suspend fun insert(todoProgress: TodoProgress): RepositoryResult<TodoProgress>

	suspend fun delete(todoProgress: TodoProgress)

}