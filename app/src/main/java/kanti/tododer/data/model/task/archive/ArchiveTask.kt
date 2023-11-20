package kanti.tododer.data.model.task.archive

import kanti.tododer.data.model.task.Task

interface ArchiveTask : Task {

	val hollow: Boolean

	companion object {
		const val HOLLOW_DEFAULT = false
	}

}
