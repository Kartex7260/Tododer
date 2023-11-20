package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.Task


interface BaseTaskDao {

	suspend fun getChildren(parentId: String): List<Task>

	suspend fun getByRowId(rowId: Long): Task?

	suspend fun getTask(id: Int): Task?

	suspend fun insert(vararg task: Task)

	suspend fun insert(task: Task): Long

	suspend fun update(vararg task: Task)

	suspend fun update(task: Task): Boolean

	suspend fun delete(vararg task: Task)

	suspend fun delete(task: Task): Boolean

	suspend fun deleteAll()

}