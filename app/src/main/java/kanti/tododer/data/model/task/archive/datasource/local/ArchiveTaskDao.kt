package kanti.tododer.data.model.task.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.archive.toArchiveTask
import kanti.tododer.data.model.task.datasource.local.BaseTaskDao

@Dao
interface ArchiveTaskDao : BaseTaskDao {

	override suspend fun getChildren(parentId: String): List<Task> {
		return getChildrenRoom(parentId).map { it.toArchiveTask() }
	}

	@Query("SELECT * FROM archive_task WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<ArchiveTaskEntity>

	override suspend fun getByRowId(rowId: Long): Task? {
		return getByRowIdRoom(rowId)?.toArchiveTask()
	}

	@Query("SELECT * FROM archive_task WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): ArchiveTaskEntity?

	override suspend fun getTodo(id: Int): Task? {
		return getTaskRoom(id)?.toArchiveTask()
	}

	@Query("SELECT * FROM archive_task WHERE id = :id")
	suspend fun getTaskRoom(id: Int): ArchiveTaskEntity?

	override suspend fun insert(vararg todo: Task) {
		insertRoom(*todo.map { it.toArchiveTaskEntity() }.toTypedArray())
	}

	override suspend fun insert(todo: Task): Long {
		return insertRoom(todo.toArchiveTaskEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg task: ArchiveTaskEntity): Array<Long>

	override suspend fun update(vararg todo: Task) {
		updateRoom(*todo.map { it.toArchiveTaskEntity() }.toTypedArray())
	}

	override suspend fun update(todo: Task): Boolean {
		return updateRoom(todo.toArchiveTaskEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg task: ArchiveTaskEntity): Int

	override suspend fun delete(vararg todo: Task) {
		deleteRoom(*todo.map { it.toArchiveTaskEntity() }.toTypedArray())
	}

	override suspend fun delete(todo: Task): Boolean {
		return deleteRoom(todo.toArchiveTaskEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg tasK: ArchiveTaskEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM archive_task")
	suspend fun deleteAllRoom()

}