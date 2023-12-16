package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.todo.TodoRepository
import kanti.tododer.data.model.todo.TodoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TodoModule {

	@Binds
	@Singleton
	fun bindTodoRepositoryImpl(repository: TodoRepositoryImpl): TodoRepository
}