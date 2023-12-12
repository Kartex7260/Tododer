package kanti.tododer.data.model.plan.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.Plan

@Entity(tableName = "plan")
data class PlanEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") override val parentId: String = "",
	override val title: String = "",
	override val remark: String = ""
) : Plan {
	@Ignore
	override val type: Todo.Type = Todo.Type.PLAN
}

fun Plan.toPlanEntity(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark
): PlanEntity {
	if (
		this is PlanEntity &&
		id == this.id &&
		parentId == this.parentId &&
		title == this.title &&
		remark == this.remark
	)
		return this
	return PlanEntity(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark
	)
}
