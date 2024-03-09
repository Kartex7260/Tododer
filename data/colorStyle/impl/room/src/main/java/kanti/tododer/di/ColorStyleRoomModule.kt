package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.colorstyle.datasource.local.ColorStyleLocalDataSource
import kanti.tododer.data.colorstyle.datasource.local.ColorStyleRoomDataSource

@Module
@InstallIn(SingletonComponent::class)
interface ColorStyleRoomModule {

    @Binds
    fun bindColorStyleRoomDataSource(
        dataSource: ColorStyleRoomDataSource
    ): ColorStyleLocalDataSource
}