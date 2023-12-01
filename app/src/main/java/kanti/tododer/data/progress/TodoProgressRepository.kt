package kanti.tododer.data.progress

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.progress.datasource.ITodoProgressLocalDataSource
import javax.inject.Inject

class TodoProgressRepository @Inject constructor(
	private val planProgressDatSource: ITodoProgressLocalDataSource
) : ITodoProgressRepository {

	override suspend fun getPlanProgress(fullId: String): RepositoryResult<TodoProgress> {
		val localResult = planProgressDatSource.getPlanProgress(fullId)
		return localResult.toRepositoryResult()
	}

	override suspend fun insert(todoProgress: TodoProgress): RepositoryResult<TodoProgress> {
		val localResult = planProgressDatSource.insert(todoProgress)
		return localResult.toRepositoryResult()
	}

	override suspend fun delete(todoProgress: TodoProgress) {
		planProgressDatSource.delete(todoProgress)
	}
}