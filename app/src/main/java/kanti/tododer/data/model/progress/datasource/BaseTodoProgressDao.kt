package kanti.tododer.data.model.progress.datasource

import kanti.tododer.data.model.progress.BaseTodoProgress

interface BaseTodoProgressDao {

	suspend fun getTodoProgress(fullId: String): BaseTodoProgress?

	suspend fun getTodoProgressByRowId(rowId: Long): BaseTodoProgress?

	suspend fun insert(planProgress: BaseTodoProgress): Long

	suspend fun delete(planProgress: BaseTodoProgress): Int

	suspend fun deleteAll()

}