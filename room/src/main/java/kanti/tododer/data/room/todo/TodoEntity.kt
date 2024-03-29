package kanti.tododer.data.room.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "parent_id") val parentId: String = "",
	val group: String? = null,
	val title: String = "",
	val remark: String = "",
	val done: Boolean = false,
	val state: String = ""
)
