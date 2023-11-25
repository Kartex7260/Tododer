package kanti.tododer.data.model.task.datasource.local

class FakeTaskRoomDataSource : TaskLocalDataSource by DefaultTaskRoomDataSource(FakeTaskDao())