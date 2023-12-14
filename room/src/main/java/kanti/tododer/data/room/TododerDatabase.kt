package kanti.tododer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.plan.PlanEntity
import kanti.tododer.data.model.progress.datasource.TodoProgressDao
import kanti.tododer.data.model.progress.datasource.TodoProgressEntity
import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.data.room.todo.TodoEntity

@Database(
	version = 1,
	entities = [
		PlanEntity::class,
		TodoProgressEntity::class,
		TodoEntity::class
	]
)
abstract class TododerDatabase : RoomDatabase() {

	abstract fun planDao(): PlanDao

	abstract fun todoDao(): TodoDao

	abstract fun planProgressDao(): TodoProgressDao

}