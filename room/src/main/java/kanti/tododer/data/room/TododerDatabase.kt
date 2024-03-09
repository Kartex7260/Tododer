package kanti.tododer.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.tododer.data.room.colorstyle.ColorStyleDao
import kanti.tododer.data.room.colorstyle.ColorStyleEntity
import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.plan.PlanEntity
import kanti.tododer.data.room.progress.PlanProgressDao
import kanti.tododer.data.room.progress.PlanProgressEntity
import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.data.room.todo.TodoEntity

@Database(
	entities = [
		ColorStyleEntity::class,
		PlanEntity::class,
		TodoEntity::class,
		PlanProgressEntity::class
	],
	version = 2,
	autoMigrations = [
		AutoMigration(from = 1, to = 2)
	],
	exportSchema = true
)
abstract class TododerDatabase : RoomDatabase() {

	abstract fun colorStyleDao(): ColorStyleDao

	abstract fun planDao(): PlanDao

	abstract fun todoDao(): TodoDao

	abstract fun progressDao(): PlanProgressDao
}