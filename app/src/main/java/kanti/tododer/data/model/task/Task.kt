package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.Todo

data class Task(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val done: Boolean = false
) : ITask {

	override val type: Todo.Type = Todo.Type.TASK

	companion object {

		val EMPTY = Task()

	}

}

fun ITask.toTask(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done
): Task {
	return Task(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}
