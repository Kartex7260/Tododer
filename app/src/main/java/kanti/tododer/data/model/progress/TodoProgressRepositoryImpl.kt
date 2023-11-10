package kanti.tododer.data.model.progress

import kanti.tododer.data.model.progress.datasource.TodoProgressLocalDataSource
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class TodoProgressRepositoryImpl @Inject constructor(
	@StandardDataQualifier private val todoProgressLocal: TodoProgressLocalDataSource
) : TodoProgressRepository by DefaultTodoProgressRepository(todoProgressLocal)