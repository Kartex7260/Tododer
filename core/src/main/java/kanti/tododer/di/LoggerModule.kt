package kanti.tododer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.tododer.util.log.StandardLog
import kanti.tododer.util.log.AndroidLogger
import kanti.tododer.util.log.Logger
import kanti.tododer.util.log.PrintLog
import kanti.tododer.util.log.PrintLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LoggerModule {

    @Binds
    @Singleton
    @StandardLog
    fun provideAndroidLogger(logger: AndroidLogger): Logger

    @Binds
    @Singleton
    @PrintLog
    fun providePrintLogger(logger: PrintLogger): Logger
}