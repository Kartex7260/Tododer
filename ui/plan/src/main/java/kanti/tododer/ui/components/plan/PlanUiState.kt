package kanti.tododer.ui.components.plan

import androidx.compose.runtime.Stable

@Stable
data class PlanUiState(
	val id: Int,
	val title: String,
	val progress: Float
)
