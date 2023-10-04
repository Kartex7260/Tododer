package kanti.tododer.data.model.task.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.asNewTaskEntity
import kanti.tododer.data.model.task.asTaskEntity

@Dao
abstract class TaskDao {

	@Query("SELECT * FROM task WHERE parent_id = :parentId")
	abstract suspend fun getChildren(parentId: String): List<TaskEntity>

	@Query("SELECT 1 FROM task WHERE rowid = :rowId")
	abstract suspend fun getByRowId(rowId: Long): TaskEntity

	@Query("SELECT * FROM task WHERE id = :id")
	abstract suspend fun getById(id: Int): List<TaskEntity>

	suspend fun get(id: Int): TaskEntity? {
		val tasks = getById(id)
		if (tasks.isEmpty())
			return null
		return tasks[0]
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun replace(task: TaskEntity): Long

	suspend fun replace(task: Task): Long = replace(task.asTaskEntity)

	suspend fun insert(task: Task): Long = replace(task.asNewTaskEntity)


	@Delete
	abstract suspend fun delete(task: TaskEntity): Int

	suspend fun delete(task: Task): Int = delete(task.asTaskEntity)

}
