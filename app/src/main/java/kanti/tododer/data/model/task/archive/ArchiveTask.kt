package kanti.tododer.data.model.task.archive

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.BaseTask

data class ArchiveTask(
	override val id: Int = 0,
	override val parentId: String = "",
	override val title: String = "",
	override val remark: String = "",
	override val done: Boolean = false,
	override val hollow: Boolean = false
) : BaseArchiveTask {

	override val type: Todo.Type = Todo.Type.TASK

}

val Todo.asArchiveTask: ArchiveTask get() {
	checkType(Todo.Type.TASK)
	if (this !is BaseArchiveTask)
		throw IllegalStateException("This todo ($this) not implementation BaseArchiveTask")
	return toArchiveTask()
}

fun BaseTask.toArchiveTask(
	id: Int = this.id,
	parentId: String = this.parentId,
	title: String = this.title,
	remark: String = this.remark,
	done: Boolean = this.done,
	hollow: Boolean? = null
): ArchiveTask {
	if (this is ArchiveTask && id == this.id && parentId == this.parentId && title == this.title &&
		remark == this.remark && done == this.done) {
		if (hollow == null || hollow == this.hollow)
			return this
	}
	return ArchiveTask(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done,
		hollow = hollow ?: if (this is BaseArchiveTask)
			this.hollow
		else
			BaseArchiveTask.HOLLOW_DEFAULT
	)
}