package kanti.tododer.data.room.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kanti.tododer.data.model.ParentId
import kanti.tododer.data.model.todo.Todo

@Entity(tableName = "todos")
data class TodoEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	@ColumnInfo(name = "parent_id") val parentIdString: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val done: Boolean = false
) : Todo {
	override val parentId: ParentId
		get() = ParentId.from(parentIdString)
}

fun Todo.toTodoEntity(
	id: Int = this.id,
	parentId: ParentId = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done
): TodoEntity {
	if (
		this is TodoEntity &&
		id == this.id &&
		parentId == this.parentId &&
		title == this.title &&
		remark == this.remark &&
		done == this.done
	)
		return this
	return TodoEntity(
		id = id,
		parentIdString = parentId.toString(),
		title = title,
		remark = remark,
		done = done
	)
}
