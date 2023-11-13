package kanti.tododer.data.model.task.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kanti.tododer.data.model.task.BaseTask
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

	override suspend fun insert(vararg task: BaseTask) {
		insertRoom(*task.map { it.toArchiveTaskEntity() }.toTypedArray())
	}

	override suspend fun insert(task: BaseTask): Long {
		return insertRoom(task.toArchiveTaskEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg task: ArchiveTaskEntity): Array<Long>

	override suspend fun update(vararg task: BaseTask) {
		updateRoom(*task.map { it.toArchiveTaskEntity() }.toTypedArray())
	}

	override suspend fun update(task: BaseTask): Boolean {
		return updateRoom(task.toArchiveTaskEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg task: ArchiveTaskEntity): Int

	override suspend fun delete(vararg task: BaseTask) {
		deleteRoom(*task.map { it.toArchiveTaskEntity() }.toTypedArray())
	}

	override suspend fun delete(task: BaseTask): Boolean {
		return deleteRoom(task.toArchiveTaskEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg tasK: ArchiveTaskEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM archive_task")
	suspend fun deleteAllRoom()

}