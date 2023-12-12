package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.task.TaskRepository
import kanti.tododer.data.task.TaskRepositoryImpl
import kanti.tododer.data.task.datasource.local.TaskLocalDataSource
import kanti.tododer.data.task.datasource.local.TaskRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TaskModule {

	@Binds
	@Singleton
	fun bindTaskRepository(repository: kanti.tododer.data.task.TaskRepositoryImpl): kanti.tododer.data.task.TaskRepository

	@Binds
	@Singleton
	fun bindTaskRoomDataSource(dataSource: kanti.tododer.data.task.datasource.local.TaskRoomDataSource): kanti.tododer.data.task.datasource.local.TaskLocalDataSource

}