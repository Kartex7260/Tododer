package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
	@StandardDataQualifier private val taskLocal: TaskLocalDataSource
) : TaskRepository by DefaultTaskRepositoryImpl(taskLocal)