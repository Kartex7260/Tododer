package kanti.tododer.data.model.progress

import androidx.annotation.FloatRange

interface ProgressRepository {

	suspend fun getProgress(planId: Long): Float?

	suspend fun setProgress(
		planId: Long,
		@FloatRange(from = 0.0, to = 1.0) progress: Float
	)

	suspend fun removeProgress(planId: Long)
}