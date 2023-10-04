package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.Task

interface TaskLocalDataSource {

	suspend fun getTask(id: Int): LocalResult<Task>

	suspend fun getChildren(fid: String): LocalResult<List<Task>>

	suspend fun insert(task: Task): Task

	suspend fun replace(task: Task): Task

	suspend fun delete(task: Task): Boolean

}