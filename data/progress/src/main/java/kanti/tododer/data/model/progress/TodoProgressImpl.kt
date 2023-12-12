package kanti.tododer.data.model.progress

import androidx.annotation.FloatRange

data class TodoProgressImpl(
	override val fullId: String,
	@FloatRange(from = 0.0, to = 1.0) override val progress: Float
) : TodoProgress

fun TodoProgress.toTodoProgress(
	fullId: String = this.fullId,
	@FloatRange(from = 0.0, to = 1.0) progress: Float = this.progress
): TodoProgress {
	if (
		this is TodoProgressImpl &&
		fullId == this.fullId &&
		progress == this.progress
	)
		return this
	return TodoProgress(
		fullId = fullId,
		progress = progress
	)
}
