package kanti.tododer.data.model.task

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.common.DefaultTodoRepositoryImpl
import kanti.tododer.data.model.common.TodoRepository
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource

class DefaultTaskRepositoryImpl(
	private val localDataSource: TaskLocalDataSource
) : TodoRepository<Task> by DefaultTodoRepositoryImpl(localDataSource), TaskRepository