package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.archive.datasource.local.ArchivePlanRoomDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanRoomDataSource
import kanti.tododer.data.model.task.archive.datasource.local.ArchiveTaskRoomDataSource
import kanti.tododer.data.progress.datasource.TodoProgressLocalDataSource
import kanti.tododer.data.progress.datasource.TodoProgressRoomDataSource
import kanti.tododer.data.model.task.datasource.local.TaskLocalDataSource
import kanti.tododer.data.model.task.datasource.local.TaskRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceBindsModule {

	@StandardDataQualifier
	@Binds
	@Singleton
	fun bindPlanRoomDataSource(dataSource: PlanRoomDataSource): PlanLocalDataSource

	@ArchiveDataQualifier
	@Binds
	@Singleton
	fun bindArchivePlanRoomDataSource(dataSource: ArchivePlanRoomDataSource): PlanLocalDataSource

	@StandardDataQualifier
	@Binds
	@Singleton
	fun bindTaskRoomDataSource(dataSource: TaskRoomDataSource): TaskLocalDataSource

	@ArchiveDataQualifier
	@Binds
	@Singleton
	fun bindArchiveTaskRoomDataSource(dataSource: ArchiveTaskRoomDataSource): TaskLocalDataSource

	@Binds
	@Singleton
	fun bindPlanProgressDataSource(dataSource: TodoProgressRoomDataSource): TodoProgressLocalDataSource

}