package kanti.tododer.data.model.task.archive.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.BaseTask
import kanti.tododer.data.model.task.archive.ArchiveTask
import kanti.tododer.data.model.task.archive.BaseArchiveTask
import kanti.tododer.data.model.task.archive.toArchiveTask

@Entity(tableName = "archive_task")
class ArchiveTaskEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val done: Boolean = false,
	override val hollow: Boolean = BaseArchiveTask.HOLLOW_DEFAULT
) : BaseArchiveTask {

	 @Ignore override val type: Todo.Type = Todo.Type.TASK

}

fun BaseTask.toArchiveTaskEntity(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done,
	hollow: Boolean? = null
): ArchiveTaskEntity {
	if (this is ArchiveTaskEntity && id == this.id && parentId == this.parentId && title == this.title &&
		remark == this.remark && done == this.done) {
		if (hollow == null || hollow == this.hollow)
			return this
	}
	return ArchiveTaskEntity(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}