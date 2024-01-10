package kanti.todoer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.todoer.data.appdata.AppDataRepository
import kanti.todoer.data.appdata.AppDataRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppDataModule {

	@Singleton
	@Binds
	fun bindAppDataRepositoryImpl(appDataRepository: AppDataRepositoryImpl): AppDataRepository
}