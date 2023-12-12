package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import kanti.tododer.data.model.task.datasource.local.TaskRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RoomTaskModule {

	@Binds
	@Singleton
	fun bindTaskRoomDataSource(dataSource: TaskRoomDataSource): TaskLocalDataSource

}