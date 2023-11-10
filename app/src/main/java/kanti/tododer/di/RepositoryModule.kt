package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanRepositoryImpl
import kanti.tododer.data.model.progress.ITodoProgressRepository
import kanti.tododer.data.model.progress.TodoProgressRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.TaskRepositoryImpl
import kanti.tododer.data.model.task.archive.ArchiveTaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

	@StandardDataQualifier
	@Binds
	@Singleton
	fun bindTaskRepository(repository: TaskRepositoryImpl): TaskRepository

	@ArchiveDataQualifier
	@Binds
	@Singleton
	fun bindArchiveTaskRepository(repository: ArchiveTaskRepository): TaskRepository

	@Binds
	@Singleton
	fun bindPlanRepository(repository: PlanRepositoryImpl): PlanRepository

	@Binds
	@Singleton
	fun bindPlanProgressRepository(repository: TodoProgressRepository): ITodoProgressRepository

}