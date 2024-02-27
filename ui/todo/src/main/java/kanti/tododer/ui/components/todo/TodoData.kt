package kanti.tododer.ui.components.todo

import androidx.compose.runtime.Immutable

@Immutable
data class TodoData(
	val id: Long = 0,
	val title: String = "",
	val remark: String = "",
	val isDone: Boolean = false
)
