package kanti.tododer.ui.components.plan

import androidx.compose.runtime.Immutable

@Immutable
data class PlanData(
	val id: Long = 0,
	val title: String = "",
	val progress: Float = 0f
)
