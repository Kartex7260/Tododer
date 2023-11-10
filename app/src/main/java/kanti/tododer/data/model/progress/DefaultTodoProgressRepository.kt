package kanti.tododer.data.model.progress

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.progress.datasource.TodoProgressLocalDataSource

class DefaultTodoProgressRepository(
	private val todoProgressLocal: TodoProgressLocalDataSource
) : TodoProgressRepository {

	override suspend fun getTodoProgress(fullId: String): RepositoryResult<BaseTodoProgress> {
		val localResult = todoProgressLocal.getTodoProgress(fullId)
		return localResult.toRepositoryResult()
	}

	override suspend fun insert(todoProgress: BaseTodoProgress): RepositoryResult<BaseTodoProgress> {
		val localResult = todoProgressLocal.insert(todoProgress)
		return localResult.toRepositoryResult()
	}

	override suspend fun delete(todoProgress: BaseTodoProgress) {
		todoProgressLocal.delete(todoProgress)
	}
}