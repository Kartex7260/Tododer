package kanti.tododer.data.progress

import androidx.annotation.FloatRange

interface TodoProgress {

	val fullId: String
	@get:FloatRange(from = 0.0, to = 1.0)
	val progress: Float

}

fun TodoProgress(
	fullId: String,
	@FloatRange(from = 0.0, to=1.0) progress: Float
): TodoProgress {
	return TodoProgressImpl(
		fullId = fullId,
		progress = progress
	)
}
