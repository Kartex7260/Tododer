package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.todo.datasource.local.TodoLocalDataSource
import kanti.tododer.data.room.todo.TodoRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TodoModule {

	@Binds
	@Singleton
	fun bindTodoRoomDataSource(dataSource: TodoRoomDataSource): TodoLocalDataSource
}