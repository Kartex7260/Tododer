package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.common.LocalResult
import kanti.tododer.data.common.localTryCatch
import kanti.tododer.data.model.common.datasource.local.DefaultTodoDaoDataSource
import kanti.tododer.data.model.common.datasource.local.TodoLocalDataSource
import kanti.tododer.data.model.task.Task

class DefaultTaskRoomDataSource(
	private val taskDao: BaseTaskDao
) : TodoLocalDataSource<Task> by DefaultTodoDaoDataSource(taskDao), TaskLocalDataSource