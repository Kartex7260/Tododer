package kanti.tododer.data.room.plan

import kanti.tododer.data.model.plan.PlanType
import javax.inject.Inject

class BasePlanDaoFiller @Inject constructor(

) : PlanDaoFiller {

	override suspend fun fill(planDao: PlanDao) {
		planDao.insert(
			PlanEntity(
				typeString = PlanType.All.toString()
			),
			PlanEntity(
				typeString = PlanType.Default.toString()
			)
		)
	}
}