package kanti.tododer.data.model.plan

import kanti.tododer.common.Const

internal val PlanAll: Plan = Plan(id = Const.PlansIds.ALL, title = "All", type = PlanType.All)
internal val PlanDefault: Plan = Plan(id = Const.PlansIds.DEFAULT, title = "Default", type = PlanType.Default)