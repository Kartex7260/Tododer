package kanti.tododer.data.model.plan.archive

import kanti.tododer.data.model.plan.BasePlan

interface BaseArchivePlan : BasePlan {

	val hollow: Boolean

	companion object {
		const val HOLLOW_DEFAULT = false
	}

}