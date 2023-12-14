package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.data.room.TododerDatabase
import kanti.tododer.data.room.plan.BasePlanDaoFiller
import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.plan.PlanDaoFiller
import kanti.tododer.data.room.plan.PlanRoomDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomPlanModule {

	@Binds
	@Singleton
	abstract fun bindPlanRoomDataSource(dataSource: PlanRoomDataSourceImpl): PlanLocalDataSource

	@Provides
	@Singleton
	fun providePlanDao(room: TododerDatabase): PlanDao {
		return room.planDao()
	}

	@Binds
	@Singleton
	abstract fun bindPlanDaoFiller(planDaoFiller: BasePlanDaoFiller): PlanDaoFiller

}