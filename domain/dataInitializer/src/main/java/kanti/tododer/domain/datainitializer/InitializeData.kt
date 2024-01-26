package kanti.tododer.domain.datainitializer

import kanti.tododer.common.Const
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.PlanType
import javax.inject.Inject

class InitializeData @Inject constructor(
	private val planRepository: PlanRepository
) {

	suspend operator fun invoke() {
		if (planRepository.isEmpty()) {
			planRepository.insert(
				listOf(
					Plan(
						id = Const.PlansIds.ALL,
						title = "All",
						type = PlanType.All
					),
					Plan(
						id = Const.PlansIds.DEFAULT,
						title = "Default",
						type = PlanType.Default
					)
				)
			)
		}
	}
}