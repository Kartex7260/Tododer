package kanti.tododer.data.model.task.datasource.local

import javax.inject.Inject

class TaskRoomDataSource @Inject constructor(
	private val taskDao: TaskDao
) : TaskLocalDataSource by DefaultTaskRoomDataSource(taskDao)