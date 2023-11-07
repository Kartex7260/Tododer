package kanti.tododer.data.model.plan.archive.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.IPlan

@Entity(tableName = "archive_plan")
data class ArchivePlanEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") override val parentId: String = "",
	override val title: String = "",
	override val remark: String = ""
) : IPlan {

	@Ignore override val type: Todo.Type = Todo.Type.PLAN

}

fun IPlan.toArchivePlanEntity(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark
): ArchivePlanEntity {
	return ArchivePlanEntity(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark
	)
}
