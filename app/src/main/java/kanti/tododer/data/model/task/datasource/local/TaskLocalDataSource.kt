package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.BaseTask

interface TaskLocalDataSource {

	suspend fun getTask(id: Int): LocalResult<BaseTask>

	suspend fun getChildren(fid: String): LocalResult<List<BaseTask>>

	suspend fun insert(vararg task: BaseTask): LocalResult<Unit>

	suspend fun insert(task: BaseTask): LocalResult<BaseTask>

	suspend fun update(vararg task: BaseTask): LocalResult<Unit>

	suspend fun update(task: BaseTask): LocalResult<BaseTask>

	suspend fun delete(vararg task: BaseTask): LocalResult<Unit>

	suspend fun delete(task: BaseTask): Boolean

	suspend fun deleteAll()

}