package kanti.tododer.data.room.plan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	val title: String = "",
	val archived: Boolean = false,
	val type: String = ""
)
