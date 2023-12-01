package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanRepositoryImpl
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanRoomDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlanModule {

	@Binds
	@Singleton
	fun bindPlanRepository(repository: PlanRepositoryImpl): PlanRepository

	@Binds
	@Singleton
	fun bindPlanRoomDataSource(dataSource: PlanRoomDataSourceImpl): PlanLocalDataSource

}