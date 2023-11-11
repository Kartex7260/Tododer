package kanti.tododer.ui.fragments.screens.todo_archive_list.viewmodel

import kanti.tododer.data.common.RepositoryResult
import kanti.tododer.data.model.plan.BasePlan

data class ArchivePlansUiState(
	val plans: List<BasePlan> = listOf(),
	val type: RepositoryResult.Type = RepositoryResult.Type.SuccessLocal,
	val process: Boolean = false
)