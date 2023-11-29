package kanti.tododer.data.model.task

import kanti.tododer.data.model.task.datasource.local.FakeTaskRoomDataSource
import kanti.tododer.data.model.task.datasource.local.TaskEntity

class FakeTaskRepository(
	val tasks: MutableList<TaskEntity> = mutableListOf()
) : TaskRepository by DefaultTaskRepositoryImpl(FakeTaskRoomDataSource(tasks))