package kanti.tododer.data.model.progress.datasource

import androidx.annotation.FloatRange
import androidx.room.Entity
import androidx.room.PrimaryKey
import kanti.tododer.data.model.progress.BaseTodoProgress
import kanti.tododer.data.model.progress.TodoProgress

@Entity(tableName = "todo_progress_cache")
data class TodoProgressEntity(
	@PrimaryKey override val fullId: String,
	@FloatRange(from = 0.0, to = 1.0) override val progress: Float
) : BaseTodoProgress

fun BaseTodoProgress.toTodoProgressEntity(
	fullId: String = this.fullId,
	progress: Float = this.progress
): TodoProgressEntity {
	return TodoProgressEntity(
		fullId = fullId,
		progress = progress
	)
}
