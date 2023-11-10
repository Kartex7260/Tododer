package kanti.tododer.data.model.task.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.Task
import kanti.tododer.data.model.task.toTask

@Dao
interface TaskDao : BaseTaskDao {

	override suspend fun getChildren(parentId: String): List<BaseTask> {
		return getChildrenRoom(parentId).map { it.toTask() }
	}

	@Query("SELECT * FROM task WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<TaskEntity>

	override suspend fun getByRowId(rowId: Long): BaseTask? {
		return getByRowIdRoom(rowId)?.toTask()
	}

	@Query("SELECT * FROM task WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): TaskEntity?

	override suspend fun getTask(id: Int): BaseTask? {
		return getTaskRoom(id)?.toTask()
	}

	@Query("SELECT * FROM task WHERE id = :id")
	suspend fun getTaskRoom(id: Int): TaskEntity?

	override suspend fun replace(task: BaseTask): Long {
		return replaceRoom(task.toTaskEntity())
	}

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun replaceRoom(task: TaskEntity): Long

	override suspend fun insert(task: BaseTask): Long {
		return insertRoom(task.toTaskEntity())
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(task: TaskEntity): Long

	override suspend fun delete(task: BaseTask): Int {
		return deleteRoom(task.toTaskEntity())
	}

	@Delete
	suspend fun deleteRoom(task: TaskEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM task")
	suspend fun deleteAllRoom()

}
