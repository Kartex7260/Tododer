package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.Task


interface BaseTaskDao {

	suspend fun getChildren(parentId: String): List<Task>

	suspend fun getByRowId(rowId: Long): Task?

	suspend fun getTask(id: Int): Task?

	suspend fun replace(task: Task): Long

	suspend fun insert(task: Task): Long

	suspend fun delete(task: Task): Int

	suspend fun deleteAll()

}