package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.data.model.progress.TodoProgressRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TodoProgressModule {

	@Binds
	@Singleton
	fun bindTodoProgressRepositoryImpl(repository: TodoProgressRepositoryImpl): TodoProgressRepository

}