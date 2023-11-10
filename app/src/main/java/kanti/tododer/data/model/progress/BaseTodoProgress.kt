package kanti.tododer.data.model.progress

import androidx.annotation.FloatRange

interface BaseTodoProgress {

	val fullId: String
	@get:FloatRange(0.0, 1.0)
	val progress: Float

}