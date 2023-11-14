package kanti.tododer.ui.viewmodelfeatures

import kanti.tododer.data.common.isNull
import kanti.tododer.data.common.isSuccess
import kanti.tododer.data.model.plan.BasePlan

interface UpdatePlanFeature : PlanRepositoryFeature {

	suspend fun updatePlan(id: Int, body: BasePlan.() -> BasePlan) {
		val plan = planRepository.getPlan(id).also { repositoryResult ->
			if (!repositoryResult.isSuccess || repositoryResult.isNull)
				return
		}.value!!
		planRepository.update(plan, body)
	}

}