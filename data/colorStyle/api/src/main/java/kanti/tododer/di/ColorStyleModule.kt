package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import kanti.tododer.data.colorstyle.ColorStyleRepository
import kanti.tododer.data.colorstyle.ColorStyleRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn
interface ColorStyleModule {

    @Binds
    @Singleton
    fun bindColorStyleRepositoryImpl(repository: ColorStyleRepositoryImpl): ColorStyleRepository
}