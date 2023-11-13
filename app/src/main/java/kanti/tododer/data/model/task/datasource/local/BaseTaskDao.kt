package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.task.BaseTask


interface BaseTaskDao {

	suspend fun getChildren(parentId: String): List<BaseTask>

	suspend fun getByRowId(rowId: Long): BaseTask?

	suspend fun getTask(id: Int): BaseTask?

	suspend fun replace(task: BaseTask): Long

	suspend fun replace(list: List<BaseTask>)

	suspend fun insert(task: BaseTask): Long

	suspend fun insert(list: List<BaseTask>)

	suspend fun delete(task: BaseTask): Int

	suspend fun delete(list: List<BaseTask>)

	suspend fun deleteAll()

}