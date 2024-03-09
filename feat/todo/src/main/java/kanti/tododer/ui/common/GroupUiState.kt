package kanti.tododer.ui.common

import androidx.compose.runtime.Immutable

@Immutable
data class GroupUiState(
	val name: String? = null,
	val expand: Boolean = true,
	val todos: List<TodoUiState> = listOf()
) {

	val selected: Boolean get() = todos.fold(true) { acc, todoUiState ->
		acc and todoUiState.selected
	}

	companion object {

		val COMPARATOR = Comparator<GroupUiState> { o1, o2 ->
			if (o1.name == null)
				return@Comparator 1
			if (o2.name == null)
				return@Comparator -1
			o1.name.compareTo(o2.name)
		}
	}
}