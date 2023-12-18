package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanRoomDataSource

@Module
@InstallIn(SingletonComponent::class)
interface PlanRoomDataSourceModule {

	@Binds
	fun bindPlanRoomDataSource(dataSource: PlanRoomDataSource): PlanLocalDataSource
}