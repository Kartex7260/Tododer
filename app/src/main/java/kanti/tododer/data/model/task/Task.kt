package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.datasource.local.TaskEntity

data class Task(
	override val id: Int = 0,
	val parentId: String = "",
	val title: String = "",
	val remark: String = "",
	val done: Boolean = false
) : Todo() {
	override val type: Todo.Type = Todo.Type.TASK
}

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
