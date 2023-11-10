package kanti.tododer.data.model.plan.archive

import kanti.tododer.data.model.plan.DefaultPlanRepositoryImpl
import kanti.tododer.data.model.plan.PlanRepository
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import kanti.tododer.di.ArchiveDataQualifier
import javax.inject.Inject

class ArchivePlanRepositoryImpl @Inject constructor(
	@ArchiveDataQualifier private val archivaPlanLocal: PlanLocalDataSource
) : PlanRepository by DefaultPlanRepositoryImpl(archivaPlanLocal)