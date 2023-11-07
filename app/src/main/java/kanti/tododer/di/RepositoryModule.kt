package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.IPlanRepository
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.progress.ITodoProgressRepository
import kanti.tododer.data.progress.TodoProgressRepository
import kanti.tododer.data.model.task.ITaskRepository
import kanti.tododer.data.model.task.TaskRepository
import kanti.tododer.data.model.task.archive.ArchiveTaskRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

	@StandardDataQualifier
	@Binds
	@Singleton
	fun bindTaskRepository(repository: TaskRepository): ITaskRepository

	@ArchiveDataQualifier
	@Binds
	@Singleton
	fun bindArchiveTaskRepository(repository: ArchiveTaskRepository): ITaskRepository

	@Binds
	@Singleton
	fun bindPlanRepository(repository: PlanRepository): IPlanRepository

	@Binds
	@Singleton
	fun bindPlanProgressRepository(repository: TodoProgressRepository): ITodoProgressRepository

}