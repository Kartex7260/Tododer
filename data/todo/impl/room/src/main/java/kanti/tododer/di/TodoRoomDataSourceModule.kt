package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import kanti.tododer.data.model.todo.datasource.local.TodoRoomDataSource

@Module
@InstallIn(SingletonComponent::class)
interface TodoRoomDataSourceModule {

	@Binds
	fun provideTodoRoomDataSource(dataSource: TodoRoomDataSource): TodoLocalDataSource
}