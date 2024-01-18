package kanti.tododer.data.model.progress

import androidx.annotation.FloatRange

data class PlanProgress(
	val planId: Long,
	@FloatRange(from = 0.0, to = 1.0) val progress: Float
)
