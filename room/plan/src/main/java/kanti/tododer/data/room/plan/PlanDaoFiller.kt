package kanti.tododer.data.room.plan

interface PlanDaoFiller {

	suspend fun fill(planDao: PlanDao)
}