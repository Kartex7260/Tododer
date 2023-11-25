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

	override suspend fun getTodo(id: Int): Plan? {
		return getPlanRoom(id)?.toPlan()
	}

	@Query("SELECT * FROM `plan` WHERE id = :id")
	suspend fun getPlanRoom(id: Int): PlanEntity?

	override suspend fun insert(vararg todo: Plan) {
		insertRoom(*todo.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun insert(todo: Plan): Long {
		return insertRoom(todo.toPlanEntity())[0]
	}

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertRoom(vararg plan: PlanEntity): Array<Long>

	override suspend fun update(vararg todo: Plan) {
		updateRoom(*todo.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun update(todo: Plan): Boolean {
		return updateRoom(todo.toPlanEntity()) == 1
	}

	@Update
	suspend fun updateRoom(vararg plan: PlanEntity): Int

	override suspend fun delete(vararg todo: Plan) {
		deleteRoom(*todo.map { it.toPlanEntity() }.toTypedArray())
	}

	override suspend fun delete(todo: Plan): Boolean {
		return deleteRoom(todo.toPlanEntity()) == 1
	}

	@Delete
	suspend fun deleteRoom(vararg plan: PlanEntity): Int

	override suspend fun deleteAll() {
		deleteAllRoom()
	}

	@Query("DELETE FROM `plan`")
	suspend fun deleteAllRoom()

}