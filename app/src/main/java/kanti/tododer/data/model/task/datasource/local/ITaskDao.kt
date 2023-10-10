package kanti.tododer.data.model.task.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.asTaskEntity

@Dao
abstract class TaskDao {

	@Query("SELECT * FROM task WHERE parent_id = :parentId")
	abstract suspend fun getChildren(parentId: String): List<TaskEntity>

	@Query("SELECT * FROM task WHERE rowid = :rowId")
	abstract suspend fun getByRowId(rowId: Long): TaskEntity?

	@Query("SELECT * FROM task WHERE id = :id")
	abstract suspend fun getTask(id: Int): TaskEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun replace(task: TaskEntity): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	abstract suspend fun insert(task: TaskEntity): Long

	@Delete
	abstract suspend fun delete(task: TaskEntity): Int

	suspend fun delete(task: Task): Int = delete(task.asTaskEntity)

	@Query("DELETE FROM task")
	abstract suspend fun deleteAll()

}

suspend fun TaskDao.insert(task: Task): Long = insert(task.asTaskEntity)

suspend fun TaskDao.replace(task: Task): Long = replace(task.asTaskEntity)
