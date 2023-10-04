package kanti.tododer.data.model.task.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kanti.tododer.data.model.task.Task

@Entity(tableName = "task")
data class TaskEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "parent_id") val parentId: String = "",
	val title: String = "",
	val remark: String = "",
	val done: Boolean = false
)

fun TaskEntity.toTask(): Task {
	return Task(id, parentId, title, remark, done)
}
