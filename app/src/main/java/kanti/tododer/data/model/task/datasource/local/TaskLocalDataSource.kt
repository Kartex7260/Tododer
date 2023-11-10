package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.Task

interface TaskLocalDataSource {

	suspend fun getTask(id: Int): LocalResult<BaseTask>

	suspend fun getChildren(fid: String): LocalResult<List<BaseTask>>

	suspend fun insert(task: BaseTask): LocalResult<BaseTask>

	suspend fun replace(task: BaseTask): LocalResult<BaseTask>

	suspend fun delete(task: BaseTask): Boolean

	suspend fun deleteAll()

}