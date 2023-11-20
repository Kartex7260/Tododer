package kanti.tododer.data.model.task.archive

import kanti.tododer.data.model.task.DefaultTaskRepositoryImpl
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import kanti.tododer.di.ArchiveDataQualifier
import javax.inject.Inject

class ArchiveTaskRepository @Inject constructor(
	@ArchiveDataQualifier private val archiveTaskLocal: TaskLocalDataSource
) : TaskRepository by DefaultTaskRepositoryImpl(archiveTaskLocal)