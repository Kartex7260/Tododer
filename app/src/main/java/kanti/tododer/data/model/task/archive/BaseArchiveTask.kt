package kanti.tododer.data.model.task.archive

import kanti.tododer.data.model.task.BaseTask

interface BaseArchiveTask : BaseTask {

	val hollow: Boolean

	companion object {
		const val HOLLOW_DEFAULT = false
	}

}