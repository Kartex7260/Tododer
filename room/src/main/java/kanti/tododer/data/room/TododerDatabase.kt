package kanti.tododer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.tododer.data.room.plan.PlanDao
import kanti.tododer.data.room.plan.PlanEntity
import kanti.tododer.data.room.progress.PlanProgressDao
import kanti.tododer.data.room.progress.PlanProgressEntity
import kanti.tododer.data.room.todo.TodoDao
import kanti.tododer.data.room.todo.TodoEntity

@Database(
	entities = [
		PlanEntity::class,
		TodoEntity::class,
		PlanProgressEntity::class
	],
	version = 1
)
abstract class TododerDatabase : RoomDatabase() {

	abstract fun planDao(): PlanDao

	abstract fun todoDao(): TodoDao

	abstract fun progressDao(): PlanProgressDao
}