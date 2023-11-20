package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.model.task.Task

interface TaskLocalDataSource {

	suspend fun getTask(id: Int): LocalResult<Task>

	suspend fun getChildren(fid: String): LocalResult<List<Task>>

	suspend fun insert(vararg task: Task): LocalResult<Unit>

	suspend fun insert(task: Task): LocalResult<Task>

	suspend fun update(vararg task: Task): LocalResult<Unit>

	suspend fun update(task: Task): LocalResult<Task>

	suspend fun delete(vararg task: Task): LocalResult<Unit>

	suspend fun delete(task: Task): Boolean

	suspend fun deleteAll()

}