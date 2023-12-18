package kanti.tododer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import kanti.tododer.data.model.todo.datasource.local.TodoRoomDataSource

@Module
@InstallIn(SingletonComponent::class)
interface TodoRoomDataSourceModule {

	@Provides
	fun provideTodoRoomDataSource(dataSource: TodoRoomDataSource): TodoLocalDataSource
}