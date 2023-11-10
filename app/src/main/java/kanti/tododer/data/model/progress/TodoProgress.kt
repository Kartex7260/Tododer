package kanti.tododer.data.model.progress

import kanti.tododer.data.model.progress.datasource.TodoProgressEntity

data class TodoProgress(
	val fullId: String,
	val progress: Float
)

val TodoProgress.asTodoProgressEntity: TodoProgressEntity
	get() {
	return TodoProgressEntity(fullId, progress)
}