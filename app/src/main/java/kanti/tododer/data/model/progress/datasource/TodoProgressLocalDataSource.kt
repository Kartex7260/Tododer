package kanti.tododer.data.model.progress.datasource

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.progress.TodoProgress

interface TodoProgressLocalDataSource {

	suspend fun getPlanProgress(fullId: String): LocalResult<TodoProgress>

	suspend fun insert(todoProgress: TodoProgress): LocalResult<TodoProgress>

	suspend fun delete(todoProgress: TodoProgress)

}