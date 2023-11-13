package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.BaseTask


interface BaseTaskDao {

	suspend fun getChildren(parentId: String): List<BaseTask>

	suspend fun getByRowId(rowId: Long): BaseTask?

	suspend fun getTask(id: Int): BaseTask?

	suspend fun insert(vararg task: BaseTask)

	suspend fun insert(task: BaseTask): Long

	suspend fun update(vararg task: BaseTask)

	suspend fun update(task: BaseTask): Boolean

	suspend fun delete(vararg task: BaseTask)

	suspend fun delete(task: BaseTask): Boolean

	suspend fun deleteAll()

}