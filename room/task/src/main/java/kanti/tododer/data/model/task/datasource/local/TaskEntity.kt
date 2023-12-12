package kanti.tododer.data.model.task.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.Task

@Entity(tableName = "task")
data class TaskEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val done: Boolean = false
) : Task {
	@Ignore
	override val type: Todo.Type = Todo.Type.TASK
}

fun Task.toTaskEntity(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done
): TaskEntity {
	if (
		this is TaskEntity &&
		id == this.id &&
		parentId == this.parentId &&
		title == this.title &&
		remark == this.remark &&
		done == this.done
	)
		return this
	return TaskEntity(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}
