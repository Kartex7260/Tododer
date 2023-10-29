package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.datasource.local.IPlanLocalDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanRoomDataSource
import kanti.tododer.data.progress.datasource.ITodoProgressLocalDataSource
import kanti.tododer.data.progress.datasource.TodoProgressRoomDataSource
import kanti.tododer.data.model.task.datasource.local.ITaskLocalDataSource
import kanti.tododer.data.model.task.datasource.local.TaskRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RoomBindsModule {

	@Binds
	@Singleton
	fun bindPlanRoomDataSource(dataSource: PlanRoomDataSource): IPlanLocalDataSource

	@Binds
	@Singleton
	fun bindTaskRoomDataSource(dataSource: TaskRoomDataSource): ITaskLocalDataSource

	@Binds
	@Singleton
	fun bindPlanProgressDataSource(dataSource: TodoProgressRoomDataSource): ITodoProgressLocalDataSource

}