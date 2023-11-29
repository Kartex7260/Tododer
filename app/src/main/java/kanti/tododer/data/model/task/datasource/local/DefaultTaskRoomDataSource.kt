package kanti.tododer.data.model.task.datasource.local

import kanti.tododer.data.model.common.datasource.local.DefaultTodoDaoDataSourceImpl
import kanti.tododer.data.model.common.datasource.local.TodoLocalDataSource
import kanti.tododer.data.model.task.Task

class DefaultTaskRoomDataSource(
	private val taskDao: BaseTaskDao
) : TodoLocalDataSource<Task> by DefaultTodoDaoDataSourceImpl(taskDao), TaskLocalDataSource