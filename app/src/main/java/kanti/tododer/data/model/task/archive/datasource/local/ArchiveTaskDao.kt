package kanti.tododer.data.model.task.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.datasource.local.BaseTaskDao
import kanti.tododer.data.model.task.toTask

@Dao
interface ArchiveTaskDao : BaseTaskDao {

	override suspend fun getChildren(parentId: String): List<BaseTask> {
		return getChildrenRoom(parentId).map { it.toTask() }
	}

	@Query("SELECT * FROM archive_task WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<ArchiveTaskEntity>

	override suspend fun getByRowId(rowId: Long): BaseTask? {
		return getByRowIdRoom(rowId)?.toTask()
	}

	@Query("SELECT * FROM archive_task WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): ArchiveTaskEntity?

	override suspend fun getTask(id: Int): BaseTask? {
		return getTaskRoom(id)?.toTask()
	}

	@Query("SELECT * FROM archive_task WHERE id = :id")
	suspend fun getTaskRoom(id: Int): ArchiveTaskEntity?

	override suspend fun replace(task: BaseTask): Long {
		return replaceRoom(task.toArchiveTaskEntity())
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(task: ArchiveTaskEntity): Long

	override suspend fun replace(list: List<BaseTask>) {
		replaceRoom(list.map { it.toArchiveTaskEntity() })
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(list: List<ArchiveTaskEntity>)

	override suspend fun insert(task: BaseTask): Long {
		return insertRoom(task.toArchiveTaskEntity())
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(task: ArchiveTaskEntity): Long

	override suspend fun insert(list: List<BaseTask>) {
		return insertRoom(list.map { it.toArchiveTaskEntity() })
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(list: List<ArchiveTaskEntity>)

	override suspend fun delete(task: BaseTask): Int {
		return deleteRoom(task.toArchiveTaskEntity())
	}

	@Delete
	suspend fun deleteRoom(task: ArchiveTaskEntity): Int

	override suspend fun delete(list: List<BaseTask>) {
		return deleteRoom(list.map { it.toArchiveTaskEntity() })
	}

	@Delete
	suspend fun deleteRoom(list: List<ArchiveTaskEntity>)

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM archive_task")
	suspend fun deleteAllRoom()

}