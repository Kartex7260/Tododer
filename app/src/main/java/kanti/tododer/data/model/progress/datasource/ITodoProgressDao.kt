package kanti.tododer.data.model.progress.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kanti.tododer.data.model.progress.TodoProgress
import kanti.tododer.data.model.progress.asTodoProgressEntity

@Dao
abstract class IPlanProgressDao {

	@Query("SELECT * FROM plan_progress_cache WHERE fullId = :fullId")
	abstract suspend fun getPlanProgress(fullId: String): TodoProgressEntity?

	@Query("SELECT * FROM plan_progress_cache WHERE rowId = :rowId")
	abstract suspend fun getPlanProgressByRowId(rowId: Long): TodoProgressEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insert(planProgress: TodoProgressEntity): Long

	@Delete
	abstract suspend fun delete(planProgress: TodoProgressEntity)

}

suspend fun IPlanProgressDao.insert(todoProgress: TodoProgress): Long {
	return insert(todoProgress.asTodoProgressEntity)
}

suspend fun IPlanProgressDao.delete(todoProgress: TodoProgress) {
	delete(todoProgress.asTodoProgressEntity)
}
