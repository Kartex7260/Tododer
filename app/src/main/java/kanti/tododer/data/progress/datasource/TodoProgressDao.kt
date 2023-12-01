package kanti.tododer.data.progress.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoProgressDao {

	@Query("SELECT * FROM plan_progress_cache WHERE fullId = :fullId")
	suspend fun getPlanProgress(fullId: String): TodoProgressEntity?

	@Query("SELECT * FROM plan_progress_cache WHERE rowId = :rowId")
	suspend fun getPlanProgressByRowId(rowId: Long): TodoProgressEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg planProgress: TodoProgressEntity): Array<Long>

	@Delete
	suspend fun delete(vararg planProgress: TodoProgressEntity)

}
