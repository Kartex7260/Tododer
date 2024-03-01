package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kanti.tododer.ui.common.TodoUiState
import kanti.tododer.ui.components.menu.NormalTodoDropdownMenu
import kanti.tododer.ui.components.todo.TodoCard
import kanti.tododer.ui.components.todo.TodoData

@Composable
fun SuperTodoCard(
	modifier: Modifier = Modifier,
	selection: Boolean = false,
	todoUiState: TodoUiState,
	onLongClick: (TodoData) -> Unit = {},
	onClick: (TodoData) -> Unit = {},
	onDoneChange: (TodoData, Boolean) -> Unit = { _, _ -> },
	onSelectChange: (TodoData, Boolean) -> Unit = { _, _ -> },
	menuOnRename: (TodoData) -> Unit = {},
	menuOnDelete: (TodoData) -> Unit = {}
) = CommonCard(
	modifier = modifier,
	selection = selection,
	state = todoUiState,
	onChangeSelect = { onSelectChange(todoUiState.data, it) }
) {
	val todoData = todoUiState.data
	TodoCard(
		onLongClick = { onLongClick(todoData) },
		onClick = { onClick(todoData) },
		onDoneChange = { onDoneChange(todoData, it) },
		todoData = todoData
	) {
		AnimatedVisibility(
			visible = !selection,
			enter = fadeIn(),
			exit = fadeOut()
		) {
			var showDropdownMenu by remember {
				mutableStateOf(false)
			}
			IconButton(onClick = { showDropdownMenu = !showDropdownMenu }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null
				)
			}
			NormalTodoDropdownMenu(
				expanded = showDropdownMenu,
				onDismissRequest = { showDropdownMenu = false },
				onRename = { menuOnRename(todoData) },
				onDelete = { menuOnDelete(todoData) }
			)
		}
	}
}