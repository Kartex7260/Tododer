package kanti.tododer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kanti.tododer.data.model.plan.archive.datasource.local.ArchivePlanDao
import kanti.tododer.data.model.plan.archive.datasource.local.ArchivePlanEntity
import kanti.tododer.data.model.plan.datasource.local.PlanDao
import kanti.tododer.data.model.plan.datasource.local.PlanEntity
import kanti.tododer.data.model.task.archive.datasource.local.ArchiveTaskDao
import kanti.tododer.data.model.task.archive.datasource.local.ArchiveTaskEntity
import kanti.tododer.data.model.progress.datasource.IPlanProgressDao
import kanti.tododer.data.model.progress.datasource.TodoProgressEntity
import kanti.tododer.data.model.task.datasource.local.TaskDao
import kanti.tododer.data.model.task.datasource.local.TaskEntity

@Database(
	version = 2,
	entities = [
		PlanEntity::class,
		ArchivePlanEntity::class,
		TaskEntity::class,
		ArchiveTaskEntity::class,
		TodoProgressEntity::class
	]
)
abstract class TododerDatabase : RoomDatabase() {

	abstract fun planDao(): PlanDao

	abstract fun archivePlanDao(): ArchivePlanDao

	abstract fun taskDao(): TaskDao

	abstract fun archiveTaskDao(): ArchiveTaskDao

	abstract fun planProgressDao(): IPlanProgressDao

}