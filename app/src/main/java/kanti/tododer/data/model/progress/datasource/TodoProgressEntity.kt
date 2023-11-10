package kanti.tododer.data.model.progress.datasource

import androidx.annotation.FloatRange
import androidx.room.Entity
import androidx.room.PrimaryKey
import kanti.tododer.data.model.progress.TodoProgress

@Entity(tableName = "plan_progress_cache")
data class TodoProgressEntity(
	@PrimaryKey val fullId: String,
	@FloatRange(from = 0.0, to = 1.0) val progress: Float
)

val TodoProgressEntity.asTodoProgress: TodoProgress
	get() {
	return TodoProgress(fullId, progress)
}
