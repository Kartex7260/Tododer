package kanti.tododer.data.model.plan.archive.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.archive.BaseArchivePlan

@Entity(tableName = "archive_plan")
data class ArchivePlanEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val hollow: Boolean = false
) : BaseArchivePlan {

	@Ignore override val type: Todo.Type = Todo.Type.PLAN

}

fun BasePlan.toArchivePlanEntity(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	hollow: Boolean? = null
): ArchivePlanEntity {
	if (this is ArchivePlanEntity && id == this.id && parentId == this.parentId && title == this.title &&
		remark == this.remark) {
		if (hollow == null || hollow == this.hollow)
			return this
	}
	return ArchivePlanEntity(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		hollow = hollow ?: if (this is BaseArchivePlan)
			this.hollow
		else
			BaseArchivePlan.HOLLOW_DEFAULT
	)
}
