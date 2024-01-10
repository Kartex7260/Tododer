package kanti.tododer.domain.plandeletebehaviour

import kanti.tododer.data.model.plan.Plan

interface PlanDeleteBehaviour {

	suspend fun delete(plansId: List<Int>): List<Plan>

	suspend fun undoDelete()

	suspend fun undoChanceRejected()
}