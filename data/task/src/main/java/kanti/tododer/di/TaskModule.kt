package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.TaskRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TaskModule {

	@Binds
	@Singleton
	fun bindTaskRepository(repository: TaskRepositoryImpl): TaskRepository

}