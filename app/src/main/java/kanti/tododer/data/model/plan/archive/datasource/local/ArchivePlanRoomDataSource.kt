package kanti.tododer.data.model.plan.archive.datasource.local

import kanti.tododer.data.model.plan.datasource.local.DefaultPlanRoomDataSource
import kanti.tododer.data.model.plan.datasource.local.PlanLocalDataSource
import javax.inject.Inject

class ArchivePlanRoomDataSource @Inject constructor(
	archivePlanDao: ArchivePlanDao
) : PlanLocalDataSource by DefaultPlanRoomDataSource(archivePlanDao)