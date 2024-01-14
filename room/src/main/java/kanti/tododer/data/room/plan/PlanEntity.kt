package kanti.tododer.data.room.plan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val title: String = "",
	val state: String = "",
	val type: String = ""
)
