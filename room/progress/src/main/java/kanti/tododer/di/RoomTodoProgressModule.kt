package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.progress.datasource.TodoProgressLocalDataSource
import kanti.tododer.data.model.progress.datasource.TodoProgressRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RoomTodoProgressModule {

	@Binds
	@Singleton
	fun bindTodoProgressRoomDataSource(dataSource: TodoProgressRoomDataSource): TodoProgressLocalDataSource

}