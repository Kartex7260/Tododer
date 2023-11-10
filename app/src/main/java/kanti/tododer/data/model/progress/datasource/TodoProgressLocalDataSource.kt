package kanti.tododer.data.model.progress.datasource

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.progress.BaseTodoProgress

interface TodoProgressLocalDataSource {

	suspend fun getTodoProgress(fullId: String): LocalResult<BaseTodoProgress>

	suspend fun insert(todoProgress: BaseTodoProgress): LocalResult<BaseTodoProgress>

	suspend fun delete(todoProgress: BaseTodoProgress)

}