package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.domain.plandeletebehaviour.PlanDeleteBehaviour
import kanti.tododer.domain.plandeletebehaviour.PlanDeleteBehaviourImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlanDeleteBehaviourModule {

	@Singleton
	@Binds
	fun bindPlanDeleteBehaviourImpl(behaviour: PlanDeleteBehaviourImpl): PlanDeleteBehaviour
}