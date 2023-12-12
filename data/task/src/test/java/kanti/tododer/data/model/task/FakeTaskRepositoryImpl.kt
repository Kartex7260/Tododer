package kanti.tododer.data.model.task

import kanti.tododer.data.model.task.datasource.local.FakeTaskRoomDataSource
import kanti.tododer.data.model.task.datasource.local.TaskEntity

class FakeTaskRepositoryImpl(
	val tasks: MutableList<kanti.tododer.data.model.task.datasource.local.TaskEntity> = mutableListOf()
) : TaskRepository by TaskRepositoryImpl(
	FakeTaskRoomDataSource(tasks)
)