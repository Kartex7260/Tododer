package kanti.tododer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kanti.tododer.common.Const
import kanti.tododer.data.model.plan.datasource.local.PlanDao
import kanti.tododer.data.progress.datasource.TodoProgressDao
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
	fun provideTaskDao(room: TododerDatabase): TaskDao {
		return room.taskDao()
	}

	@Provides
	@Singleton
	fun providePlanProgressDao(room: TododerDatabase): TodoProgressDao {
		return room.planProgressDao()
	}

}