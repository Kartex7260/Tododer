package kanti.tododer.data.model.task

import kanti.tododer.data.model.task.datasource.local.FakeTaskRoomDataSource

class FakeTaskRepository : TaskRepository by DefaultTaskRepositoryImpl(FakeTaskRoomDataSource())