package kanti.tododer.data.model.task

import kanti.tododer.data.model.FullIds
import kanti.tododer.data.model.task.datasource.local.TaskEntity

data class Task(
	val id: Int = 0,
	val parentId: String = "",
	val title: String = "",
	val remark: String = "",
	val done: Boolean = false
)

val Task.fullId: String
	get() = FullIds.from(this)

val Task.asTaskEntity: TaskEntity
	get() {
		return TaskEntity(
			id = id,
			parentId = parentId,
			title = title,
			remark = remark,
			done = done
		)
	}
