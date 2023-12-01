package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.common.result.GetLocalResult
import kanti.tododer.data.model.task.Task

interface TaskLocalDataSource {

	suspend fun getTask(id: Int): GetLocalResult<Task>

	suspend fun getChildren(fid: String): Result<List<Task>>

	suspend fun insert(task: Task): Result<Task>

	suspend fun insert(vararg task: Task): Result<Unit>

	suspend fun delete(vararg task: Task): Result<Unit>

	suspend fun deleteAll(): Result<Unit>

}