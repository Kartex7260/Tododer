package kanti.tododer.data.progress

import kanti.tododer.data.model.common.result.GetRepositoryResult
import kanti.tododer.data.model.common.result.asRepositoryResult
import kanti.tododer.data.progress.datasource.TodoProgressLocalDataSource
import javax.inject.Inject

class TodoProgressRepositoryImpl @Inject constructor(
	private val localDataSource: TodoProgressLocalDataSource
) : TodoProgressRepository {

	override suspend fun getPlanProgress(fullId: String): GetRepositoryResult<TodoProgress> {
		return localDataSource.getPlanProgress(fullId).asRepositoryResult
	}

	override suspend fun insert(todoProgress: TodoProgress): Result<TodoProgress> {
		return localDataSource.insert(todoProgress)
	}

	override suspend fun insert(vararg todoProgress: TodoProgress): Result<Unit> {
		return localDataSource.insert(*todoProgress)
	}

	override suspend fun delete(vararg todoProgress: TodoProgress): Result<Unit> {
		return localDataSource.delete(*todoProgress)
	}
}