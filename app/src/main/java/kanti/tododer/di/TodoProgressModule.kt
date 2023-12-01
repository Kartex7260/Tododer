package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.progress.datasource.TodoProgressLocalDataSource
import kanti.tododer.data.progress.datasource.TodoProgressRoomDataSource
import kanti.tododer.data.progress.TodoProgressRepository
import kanti.tododer.data.progress.TodoProgressRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TodoProgressModule {

	@Binds
	@Singleton
	fun bindPlanProgressDataSource(dataSource: TodoProgressRoomDataSource): TodoProgressLocalDataSource

	@Binds
	@Singleton
	fun bindPlanProgressRepository(repository: TodoProgressRepositoryImpl): TodoProgressRepository

}