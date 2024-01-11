package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlanModule {

	@Binds
	@Singleton
	fun bindPlanRepository(repository: PlanRepositoryImpl): PlanRepository

}