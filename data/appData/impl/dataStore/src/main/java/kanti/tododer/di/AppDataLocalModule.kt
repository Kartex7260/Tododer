package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.datastore.AppDataStoreDataSource
import kanti.todoer.data.appdata.AppDataLocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppDataLocalModule {

	@Singleton
	@Binds
	fun bindAppDataStoreDataSource(dataSource: AppDataStoreDataSource): AppDataLocalDataSource
}