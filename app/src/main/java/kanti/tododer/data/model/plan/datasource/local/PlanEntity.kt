package kanti.tododer.data.model.plan.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan

@Entity(tableName = "plan")
data class PlanEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") override val parentId: String = "",
	override val title: String = "",
	override val remark: String = ""
) : BasePlan {

	@Ignore override val type: Todo.Type = Todo.Type.PLAN

}

fun BasePlan.toPlanEntity(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark
): PlanEntity {
	return PlanEntity(
		id,
		parentId,
		title,
		remark
	)
}
