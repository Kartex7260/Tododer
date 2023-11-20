package kanti.tododer.common.features

import kanti.tododer.data.common.isNull
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.plan.Plan

interface UpdatePlanFeature : PlanRepositoryFeature {

	suspend fun updatePlan(id: Int, body: Plan.() -> Plan) {
		val plan = planRepository.getPlan(id).also { repositoryResult ->
			if (!repositoryResult.isSuccess || repositoryResult.isNull)
				return
		}.value!!
		planRepository.update(plan, body)
	}

}