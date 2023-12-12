package kanti.tododer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kanti.sl.StateLanguage

@Module
@InstallIn(SingletonComponent::class)
object StateLanguageModule {

	@Singleton
	@Provides
	fun provideStateLanguage(): StateLanguage {
		return StateLanguage {

		}
	}

}