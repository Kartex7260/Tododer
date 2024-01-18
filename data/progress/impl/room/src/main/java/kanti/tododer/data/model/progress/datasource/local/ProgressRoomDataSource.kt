package kanti.tododer.data.model.progress.datasource.local

import kanti.tododer.data.room.progress.PlanProgressDao
import kanti.tododer.data.room.progress.PlanProgressEntity
import javax.inject.Inject

class ProgressRoomDataSource @Inject constructor(
	private val planProgressDao: PlanProgressDao
) : ProgressLocalDataSource {

	override suspend fun getProgress(planId: Long): Float? {
		return planProgressDao.getProgress(planId)
	}

	override suspend fun setProgress(planId: Long, progress: Float) {
		planProgressDao.insert(
			progress = PlanProgressEntity(planId, progress)
		)
	}

	override suspend fun removeProgress(planId: Long) {
		planProgressDao.deleteProgress(planId)
	}
}