package kanti.tododer.data.model.plan.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kanti.tododer.data.model.plan.Plan

@Entity(tableName = "plan")
data class PlanEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "parent_id") val parentId: String = "",
	val title: String = "",
	val remark: String = ""
)

fun PlanEntity.toPlan(): Plan {
	return Plan(id, parentId, title, remark)
}
