package kanti.tododer.data.room.plan

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

	@Query("SELECT * FROM plans")
	suspend fun getAll(): List<PlanEntity>

	@Query("SELECT * FROM plans WHERE id IN (:plansId)")
	suspend fun getAll(plansId: List<Int>): List<PlanEntity>

	@Query("SELECT * FROM plans WHERE state like '%' || :state || '%' AND type = :type")
	fun getAll(state: String, type: String): Flow<List<PlanEntity>>

	@Query("SELECT * FROM plans WHERE type = :type LIMIT 1")
	fun getFromType(type: String): Flow<PlanEntity?>

	@Query("SELECT * FROM plans WHERE rowId = :rowId LIMIT 1")
	suspend fun getByRowId(rowId: Long): PlanEntity?

	@Query("SELECT * FROM plans WHERE id = :id LIMIT 1")
	suspend fun getPlan(id: Int): PlanEntity?

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(plan: PlanEntity): Long

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(plans: List<PlanEntity>)

	@Query("UPDATE plans SET title = :title WHERE rowId in " +
			"(SELECT rowId FROM plans WHERE id = :planId LIMIT 1)")
	suspend fun updateTitle(planId: Int, title: String)

	@Query("DELETE FROM plans WHERE id in (:planIds)")
	suspend fun delete(planIds: List<Int>)

	@Query("SELECT COUNT(*) FROM plans")
	suspend fun count(): Int

	@Query("DELETE FROM plans")
	suspend fun deleteAll()
}