package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanRoomDataSource
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import kanti.tododer.data.model.task.datasource.local.TaskRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RoomBindsModule {

	@Binds
	@Singleton
	fun bindPlanRoomDataSource(dataSource: PlanRoomDataSource): PlanLocalDataSource

	@Binds
	@Singleton
	fun bindTaskRoomDataSource(dataSource: TaskRoomDataSource): TaskLocalDataSource

}