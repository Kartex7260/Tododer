package kanti.tododer.data.model.task.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

	@Query("SELECT * FROM task WHERE parent_id = :parentId")
	suspend fun getChildren(parentId: String): List<TaskEntity>

	@Query("SELECT * FROM task WHERE rowid = :rowId")
	suspend fun getByRowId(rowId: Long): TaskEntity?

	@Query("SELECT * FROM task WHERE id = :id")
	suspend fun getTask(id: Int): TaskEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg task: TaskEntity): Array<Long>

	@Delete
	suspend fun delete(vararg task: TaskEntity): Int

	@Query("DELETE FROM task")
	suspend fun deleteAll()

}
