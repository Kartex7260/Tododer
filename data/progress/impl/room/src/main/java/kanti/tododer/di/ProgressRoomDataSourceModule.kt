package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.model.progress.datasource.local.ProgressLocalDataSource
import kanti.tododer.data.model.progress.datasource.local.ProgressRoomDataSource

@Module
@InstallIn(SingletonComponent::class)
interface ProgressRoomDataSourceModule {

	@Binds
	fun bindProgressRoomDataSource(dataSource: ProgressRoomDataSource): ProgressLocalDataSource
}