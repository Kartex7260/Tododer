package kanti.tododer.ui.components.todo

import androidx.compose.runtime.Stable

@Stable
data class TodoData(
	val id: Int = 0,
	val title: String = "",
	val remark: String = "",
	val isDone: Boolean = false
)