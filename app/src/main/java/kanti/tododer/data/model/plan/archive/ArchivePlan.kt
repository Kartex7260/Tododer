package kanti.tododer.data.model.plan.archive

import kanti.tododer.data.model.plan.Plan

interface ArchivePlan : Plan {

	val hollow: Boolean

	companion object {
		const val HOLLOW_DEFAULT = false
	}

}
