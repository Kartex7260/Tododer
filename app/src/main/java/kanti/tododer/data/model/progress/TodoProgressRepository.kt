package kanti.tododer.data.model.progress

import kanti.tododer.data.common.RepositoryResult

interface TodoProgressRepository {

	suspend fun getTodoProgress(fullId: String): RepositoryResult<BaseTodoProgress>

	suspend fun insert(todoProgress: BaseTodoProgress): RepositoryResult<BaseTodoProgress>

	suspend fun delete(todoProgress: BaseTodoProgress)

}