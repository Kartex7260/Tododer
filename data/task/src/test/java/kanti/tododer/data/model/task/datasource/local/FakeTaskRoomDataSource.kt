package kanti.tododer.data.model.task.datasource.local

class FakeTaskRoomDataSource(
	val tasks: MutableList<kanti.tododer.data.model.task.datasource.local.TaskEntity> = mutableListOf()
) : TaskLocalDataSource by kanti.tododer.data.model.task.datasource.local.TaskRoomDataSource(
	FakeTaskDao(tasks)
)