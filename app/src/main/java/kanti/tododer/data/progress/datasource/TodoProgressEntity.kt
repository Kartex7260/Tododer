package kanti.tododer.data.progress.datasource

import androidx.annotation.FloatRange
import androidx.room.Entity
import androidx.room.PrimaryKey
import kanti.tododer.data.progress.TodoProgress

@Entity(tableName = "plan_progress_cache")
data class TodoProgressEntity(
	@PrimaryKey override val fullId: String,
	@FloatRange(from = 0.0, to = 1.0) override val progress: Float
) : TodoProgress

fun TodoProgress.toTodoProgressEntity(
	fullId: String = this.fullId,
	@FloatRange(from = 0.0, to = 1.0) progress: Float = this.progress
): TodoProgressEntity {
	if (
		this is TodoProgressEntity &&
		fullId == this.fullId &&
		progress == this.progress
	)
		return this
	return TodoProgressEntity(
		fullId = fullId,
		progress = progress
	)
}
