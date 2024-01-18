package kanti.tododer.data.room.progress

import androidx.annotation.FloatRange
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plan_progress")
data class PlanProgressEntity(
	@PrimaryKey
	@ColumnInfo(name = "plan_id")
	val planId: Long,
	@FloatRange(from = 0.0, to = 1.0) val progress: Float
)
