package kanti.tododer.data.model.plan.datasource.local

import kanti.tododer.data.model.common.datasource.local.TodoDao
import kanti.tododer.data.model.plan.Plan

interface BasePlanDao : TodoDao<Plan>
