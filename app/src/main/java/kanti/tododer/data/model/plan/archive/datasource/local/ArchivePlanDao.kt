package kanti.tododer.data.model.plan.archive.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.archive.toArchivePlan
import kanti.tododer.data.model.plan.datasource.local.BasePlanDao

@Dao
interface ArchivePlanDao : BasePlanDao {

	override suspend fun getChildren(parentId: String): List<Plan> {
		return getChildrenRoom(parentId).map { it.toArchivePlan() }
	}

	@Query("SELECT * FROM archive_plan WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<ArchivePlanEntity>

	override suspend fun getByRowId(rowId: Long): Plan? {
		return getByRowIdRoom(rowId)?.toArchivePlan()
	}

	@Query("SELECT * FROM archive_plan WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): ArchivePlanEntity?

	override suspend fun getPlan(id: Int): Plan? {
		return getPlanRoom(id)?.toArchivePlan()
	}

	@Query("SELECT * FROM archive_plan WHERE id = :id")
	suspend fun getPlanRoom(id: Int): ArchivePlanEntity?

	override suspend fun insert(vararg plan: Plan) {
		insertRoom(*plan.map { it.toArchivePlanEntity() }.toTypedArray())
	}

	override suspend fun insert(plan: Plan): Long {
		return insertRoom(plan.toArchivePlanEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg plan: ArchivePlanEntity): Array<Long>

	override suspend fun update(vararg plan: Plan) {
		updateRoom(*plan.map { it.toArchivePlanEntity() }.toTypedArray())
	}

	override suspend fun update(plan: Plan): Boolean {
		return updateRoom(plan.toArchivePlanEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg plan: ArchivePlanEntity): Int

	override suspend fun delete(vararg plan: Plan) {
		deleteRoom(*plan.map { it.toArchivePlanEntity() }.toTypedArray())
	}

	override suspend fun delete(plan: Plan): Boolean {
		return deleteRoom(plan.toArchivePlanEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg plan: ArchivePlanEntity): Int

	override suspend fun deleteAll() = deleteAllRoom()

	@Query("DELETE FROM archive_plan")
	suspend fun deleteAllRoom()

}