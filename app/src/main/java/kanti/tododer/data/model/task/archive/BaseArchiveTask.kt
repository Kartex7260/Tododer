package kanti.tododer.data.model.task.archive

import kanti.tododer.data.model.common.Todo
import kanti.tododer.data.model.task.BaseTask

interface BaseArchiveTask : BaseTask {

	val hollow: Boolean

	companion object {
		const val HOLLOW_DEFAULT = false
	}

}

val Todo.asBaseArchiveTask: BaseArchiveTask get() {
	return this as BaseArchiveTask
}