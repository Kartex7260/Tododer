package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.settings.DataStoreSettingsRepository
import kanti.tododer.data.model.settings.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreSettingsModule {

	@Singleton
	@Binds
	fun bindDataStoreSettingsRepository(repository: DataStoreSettingsRepository): SettingsRepository
}