package kanti.tododer.data.model.plan

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.common.toRepositoryResult
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.di.StandardDataQualifier
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
	@StandardDataQualifier private val planLocal: PlanLocalDataSource
) : PlanRepository by DefaultPlanRepositoryImpl(planLocal)