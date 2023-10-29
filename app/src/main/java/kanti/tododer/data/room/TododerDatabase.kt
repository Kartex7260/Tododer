package kanti.tododer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.tododer.data.model.plan.datasource.local.IPlanDao
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.progress.datasource.IPlanProgressDao
import kanti.tododer.data.progress.datasource.TodoProgressEntity
import kanti.tododer.data.model.task.datasource.local.TaskDao
import kanti.tododer.data.model.task.datasource.local.TaskEntity

@Database(
	version = 1,
	entities = [
		PlanEntity::class,
		TaskEntity::class,
		TodoProgressEntity::class
	]
)
abstract class TododerDatabase : RoomDatabase() {

	abstract fun planDao(): IPlanDao

	abstract fun taskDao(): TaskDao

	abstract fun planProgressDao(): IPlanProgressDao

}