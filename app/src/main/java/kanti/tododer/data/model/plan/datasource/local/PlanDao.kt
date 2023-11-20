package kanti.tododer.data.model.plan.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.toPlan

@Dao
interface PlanDao : BasePlanDao {


	override suspend fun getChildren(parentId: String): List<Plan> {
		return getChildrenRoom(parentId).map { it.toPlan() }
	}

	@Query("SELECT * FROM `plan` WHERE parent_id = :parentId")
	suspend fun getChildrenRoom(parentId: String): List<PlanEntity>

	override suspend fun getByRowId(rowId: Long): Plan? {
		return getByRowIdRoom(rowId)?.toPlan()
	}

	@Query("SELECT * FROM `plan` WHERE rowid = :rowId")
	suspend fun getByRowIdRoom(rowId: Long): PlanEntity?

	override suspend fun getPlan(id: Int): Plan? {
		return getPlanRoom(id)?.toPlan()
	}

	@Query("SELECT * FROM `plan` WHERE id = :id")
	suspend fun getPlanRoom(id: Int): PlanEntity?

	override suspend fun insert(vararg plan: Plan) {
		insertRoom(*plan.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun insert(plan: Plan): Long {
		return insertRoom(plan.toPlanEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg plan: PlanEntity): Array<Long>

	override suspend fun update(vararg plan: Plan) {
		updateRoom(*plan.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun update(plan: Plan): Boolean {
		return updateRoom(plan.toPlanEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg plan: PlanEntity): Int

	override suspend fun delete(vararg plan: Plan) {
		deleteRoom(*plan.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun delete(plan: Plan): Boolean {
		return deleteRoom(plan.toPlanEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg plan: PlanEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM `plan`")
	suspend fun deleteAllRoom()

}