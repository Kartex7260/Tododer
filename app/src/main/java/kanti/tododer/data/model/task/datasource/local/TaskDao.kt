package kanti.tododer.data.model.task.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kanti.tododer.data.model.task.BaseTask
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

	override suspend fun insert(vararg task: BaseTask) {
		insertRoom(*task.map { it.toTaskEntity() }.toTypedArray())
	}

	override suspend fun insert(task: BaseTask): Long {
		return insertRoom(task.toTaskEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg task: TaskEntity): Array<Long>

	override suspend fun update(vararg task: BaseTask) {
		updateRoom(*task.map { it.toTaskEntity() }.toTypedArray())
	}

	override suspend fun update(task: BaseTask): Boolean {
		return updateRoom(task.toTaskEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg task: TaskEntity): Int

	override suspend fun delete(vararg task: BaseTask) {
		deleteRoom(*task.map { it.toTaskEntity() }.toTypedArray())
	}

	override suspend fun delete(task: BaseTask): Boolean {
		return deleteRoom(task.toTaskEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg tasK: TaskEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM task")
	suspend fun deleteAllRoom()

}
