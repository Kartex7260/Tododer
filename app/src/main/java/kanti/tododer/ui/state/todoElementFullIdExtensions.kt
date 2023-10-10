package kanti.tododer.ui.state

import kanti.tododer.data.model.plan.fullId
import kanti.tododer.data.model.task.fullId

val TodoElement.fullId: String
	get() = when (type) {
		TodoElement.Type.PLAN -> toPlan.fullId
		TodoElement.Type.TASK -> toTask.fullId
	}