package kanti.tododer.data.model.task.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArchiveTaskDao {

	@Query("SELECT * FROM archive_task WHERE parent_id = :parentId")
	suspend fun getChildren(parentId: String): List<ArchiveTaskEntity>

	@Query("SELECT * FROM archive_task WHERE rowid = :rowId")
	suspend fun getByRowId(rowId: Long): ArchiveTaskEntity?

	@Query("SELECT * FROM archive_task WHERE id = :id")
	suspend fun getTask(id: Int): ArchiveTaskEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replace(task: ArchiveTaskEntity): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(task: ArchiveTaskEntity): Long

	@Delete
	suspend fun delete(task: ArchiveTaskEntity): Int

	@Query("DELETE FROM archive_task")
	suspend fun deleteAll()

}