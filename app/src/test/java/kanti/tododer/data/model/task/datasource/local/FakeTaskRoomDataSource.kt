package kanti.tododer.data.model.task.datasource.local

class FakeTaskRoomDataSource(
	val tasks: MutableList<TaskEntity> = mutableListOf()
) : TaskLocalDataSource by DefaultTaskRoomDataSource(FakeTaskDao(tasks))