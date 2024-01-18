package kanti.tododer.data.model.progress

import kanti.tododer.data.model.progress.datasource.local.ProgressLocalDataSource
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
	private val localDataSource: ProgressLocalDataSource
) : ProgressRepository {

	override suspend fun getProgress(planId: Long): Float? {
		return localDataSource.getProgress(planId)
	}

	override suspend fun setProgress(planId: Long, progress: Float) {
		localDataSource.setProgress(planId, progress)
	}

	override suspend fun removeProgress(planId: Long) {
		localDataSource.removeProgress(planId)
	}
}