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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

	@Binds
	@Singleton
	fun bindTaskRepository(repository: TaskRepository): ITaskRepository

	@Binds
	@Singleton
	fun bindPlanRepository(repository: PlanRepository): IPlanRepository

	@Binds
	@Singleton
	fun bindPlanProgressRepository(repository: TodoProgressRepository): ITodoProgressRepository

}