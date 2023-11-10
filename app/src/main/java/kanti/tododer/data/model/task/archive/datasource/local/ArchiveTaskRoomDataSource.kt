package kanti.tododer.data.model.task.archive.datasource.local

import kanti.tododer.data.model.task.datasource.local.DefaultTaskRoomDataSource
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import javax.inject.Inject

class ArchiveTaskRoomDataSource @Inject constructor(
	private val archiveTaskDao: ArchiveTaskDao
) : TaskLocalDataSource by DefaultTaskRoomDataSource(archiveTaskDao)