package kanti.tododer.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StandardDataQualifier

@Qualifier
@Retention
annotation class ArchiveDataQualifier