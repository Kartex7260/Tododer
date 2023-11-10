package kanti.tododer.data.model.progress

import androidx.annotation.FloatRange

data class TodoProgress(
	override val fullId: String,
	@FloatRange(0.0, 1.0) override val progress: Float
) : BaseTodoProgress

fun BaseTodoProgress.toTodoProgress(
	fullId: String = this.fullId,
	progress: Float = this.progress
): TodoProgress {
	return TodoProgress(
		fullId = fullId,
		progress = progress
	)
}