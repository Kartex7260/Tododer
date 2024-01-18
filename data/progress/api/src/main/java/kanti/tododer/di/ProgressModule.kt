package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.progress.ProgressRepository
import kanti.tododer.data.model.progress.ProgressRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProgressModule {

	@Singleton
	@Binds
	fun bindProgressRepositoryImpl(repository: ProgressRepositoryImpl): ProgressRepository
}
