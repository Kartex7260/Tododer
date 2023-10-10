package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.Task

interface ITaskLocalDataSource {

	suspend fun getTask(id: Int): LocalResult<Task>

	suspend fun getChildren(fid: String): LocalResult<List<Task>>

	suspend fun insert(task: Task): LocalResult<Task>

	suspend fun replace(task: Task): LocalResult<Task>

	suspend fun delete(task: Task): Boolean

	suspend fun deleteAll()

}