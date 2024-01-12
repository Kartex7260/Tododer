package kanti.tododer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kanti.tododer.common.Const
import kanti.tododer.data.room.TododerDatabase
import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.todo.TodoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

	@Provides
	@Singleton
	fun provideTododerDatabase(@ApplicationContext context: Context): TododerDatabase {
		return Room.databaseBuilder(context, TododerDatabase::class.java, Const.DATABASE_NAME)
			.fallbackToDestructiveMigration()
			.build()
	}

	@Provides
	@Singleton
	fun providePlanDao(database: TododerDatabase): PlanDao {
		return database.planDao()
	}

	@Provides
	@Singleton
	fun provideTodoDao(database: TododerDatabase): TodoDao {
		return database.todoDao()
	}
}