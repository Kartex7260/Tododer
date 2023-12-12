package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.common.checkType

data class TaskImpl(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val done: Boolean = false
) : Task {
	override val type: Todo.Type = Todo.Type.TASK
}

val Todo.asTask: Task
	get() {
	checkType(Todo.Type.TASK)
	if (this !is Task)
		throw IllegalStateException("This todo($this) not implementation Task")
	return toTask()
}

fun Task.toTask(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done
): Task {
	if (
		this is TaskImpl &&
		id == this.id &&
		parentId == this.parentId &&
		title == this.title &&
		remark == this.remark &&
		done == this.done
	)
		return this
	return Task(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}
