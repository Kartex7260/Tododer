package kanti.tododer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kanti.tododer.common.Const
import kanti.tododer.data.model.plan.archive.datasource.local.ArchivePlanDao
import kanti.tododer.data.model.plan.archive.datasource.local.ArchivePlanEntity
import kanti.tododer.data.model.plan.datasource.local.BasePlanDao
import kanti.tododer.data.model.plan.datasource.local.PlanDao
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.task.archive.datasource.local.ArchiveTaskDao
import kanti.tododer.data.model.task.datasource.local.BaseTaskDao
import kanti.tododer.data.model.progress.datasource.IPlanProgressDao
import kanti.tododer.data.model.task.datasource.local.TaskDao
import kanti.tododer.data.room.TododerDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

	@Provides
	@Singleton
	fun provideTododerDatabase(@ApplicationContext context: Context): TododerDatabase {
		return Room.databaseBuilder(
			context = context,
			klass = TododerDatabase::class.java,
			name = Const.DATABASE_NAME
		).build()
	}

	@Provides
	@Singleton
	fun providePlanDao(room: TododerDatabase): PlanDao {
		return room.planDao()
	}

	@Provides
	@Singleton
	fun provideArchivePlanDao(room: TododerDatabase): ArchivePlanDao {
		return room.archivePlanDao()
	}

	@Provides
	@Singleton
	fun provideTaskDao(room: TododerDatabase): TaskDao {
		return room.taskDao()
	}

	@Provides
	@Singleton
	fun provideArchiveTaskDao(room: TododerDatabase): ArchiveTaskDao {
		return room.archiveTaskDao()
	}

	@Provides
	@Singleton
	fun providePlanProgressDao(room: TododerDatabase): IPlanProgressDao {
		return room.planProgressDao()
	}

}