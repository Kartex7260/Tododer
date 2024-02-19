package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.data.colorstyle.ColorStyleRepository
import kanti.tododer.data.colorstyle.ColorStyleRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ColorStyleModule {

    @Binds
    @Singleton
    fun bindColorStyleRepositoryImpl(repository: ColorStyleRepositoryImpl): ColorStyleRepository
}