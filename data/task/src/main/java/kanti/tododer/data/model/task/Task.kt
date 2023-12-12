package kanti.tododer.data.model.task

import kanti.tododer.data.model.common.RemarkOwner
import kanti.tododer.data.model.common.TitleOwner
import kanti.tododer.data.model.common.Todo

interface Task : Todo, TitleOwner, RemarkOwner {

	val done: Boolean

}

fun Task(
	id: Int = 0,
	parentId: String = "",
	title: String = "",
	remark: String = "",
	done: Boolean = false
): Task {
	return TaskImpl(
		id = id,
		parentId = parentId,
		title = title,
		remark = remark,
		done = done
	)
}
