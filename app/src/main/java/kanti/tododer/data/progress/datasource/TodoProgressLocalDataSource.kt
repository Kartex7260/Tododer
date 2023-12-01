package kanti.tododer.data.progress.datasource

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.progress.TodoProgress

interface TodoProgressLocalDataSource {

	suspend fun getPlanProgress(fullId: String): GetLocalResult<TodoProgress>

	suspend fun insert(todoProgress: TodoProgress): Result<TodoProgress>

	suspend fun insert(vararg todoProgress: TodoProgress): Result<Unit>

	suspend fun delete(vararg todoProgress: TodoProgress): Result<Unit>

}