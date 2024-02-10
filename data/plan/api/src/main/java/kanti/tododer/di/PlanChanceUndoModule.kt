package kanti.tododer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.services.chanceundo.ChanceUndo
import kanti.tododer.services.chanceundo.ChanceUndoImpl
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.StandardLog

@Module
@InstallIn(SingletonComponent::class)
object PlanChanceUndoModule {

	@PlansQualifier
	@Provides
	fun providePlansChanceUndo(@StandardLog logger: Logger): ChanceUndo<List<Plan>> {
		return ChanceUndoImpl(logger = logger)
	}
}