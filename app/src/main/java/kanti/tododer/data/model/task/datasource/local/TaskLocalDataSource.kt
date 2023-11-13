package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.BaseTask

interface TaskLocalDataSource {

	suspend fun getTask(id: Int): LocalResult<BaseTask>

	suspend fun getChildren(fid: String): LocalResult<List<BaseTask>>

	suspend fun insert(task: BaseTask): LocalResult<BaseTask>

	suspend fun insert(task: List<BaseTask>): LocalResult<Unit>

	suspend fun replace(task: BaseTask): LocalResult<BaseTask>

	suspend fun replace(task: List<BaseTask>): LocalResult<Unit>

	suspend fun delete(task: BaseTask): Boolean

	suspend fun delete(tasK: List<BaseTask>): LocalResult<Unit>

	suspend fun deleteAll()

}