package kanti.tododer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.common.TodoUiState
import kanti.tododer.ui.common.anim.allowSelectionColor
import kanti.tododer.ui.components.menu.NormalTodoDropdownMenu
import kanti.tododer.ui.components.todo.TodoCard
import kanti.tododer.ui.components.todo.TodoData

@Composable
fun SuperTodoCard(
	modifier: Modifier = Modifier,
	selectionStyle: Set<MultiSelectionStyle> = setOf(MultiSelectionStyle.ColorFill),
	selection: Boolean = false,
	todoUiState: TodoUiState,
	allowGrouping: Boolean = true,
	onLongClick: (TodoData) -> Unit = {},
	onClick: (TodoData) -> Unit = {},
	onDoneChange: (TodoData, Boolean) -> Unit = { _, _ -> },
	onChangeSelect: (TodoData, Boolean) -> Unit = { _, _ -> },
	menuOnAddToGroup: (TodoData) -> Unit = {},
	menuOnRename: (TodoData) -> Unit = {},
	menuOnSelect: (TodoData) -> Unit = {},
	menuOnDelete: (TodoData) -> Unit = {}
) {
	val colorFillEnable = selectionStyle.contains(MultiSelectionStyle.ColorFill)
	val checkboxEnable = selectionStyle.contains(MultiSelectionStyle.Checkbox)
	val selected = selection && todoUiState.selected

	CommonCard(
		modifier = modifier,
		selection = selection && checkboxEnable,
		state = todoUiState,
		onChangeSelect = { onChangeSelect(todoUiState.data, it) },
		checkboxColors = CheckboxDefaults.colors(
			checkedColor = allowSelectionColor(
				allow = colorFillEnable,
				selected = selected,
				selectedColor = MaterialTheme.colorScheme.tertiary,
				normalColor = MaterialTheme.colorScheme.primary
			),
			checkmarkColor = MaterialTheme.colorScheme.surface
		)
	) {
		val todoData = todoUiState.data

		TodoCard(
			checkboxColors = CheckboxDefaults.colors(
				checkedColor = allowSelectionColor(
					allow = colorFillEnable,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.tertiary,
					normalColor = MaterialTheme.colorScheme.primary
				),
				uncheckedColor = allowSelectionColor(
					allow = colorFillEnable,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.onTertiaryContainer,
					normalColor = MaterialTheme.colorScheme.onSurfaceVariant
				),
				checkmarkColor = allowSelectionColor(
					allow = colorFillEnable,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.tertiaryContainer,
					normalColor = MaterialTheme.colorScheme.surfaceVariant
				)
			),
			cardColors = CardDefaults.cardColors(
				containerColor = allowSelectionColor(
					allow = colorFillEnable,
					selected = selected,
					selectedColor = MaterialTheme.colorScheme.tertiaryContainer,
					normalColor = MaterialTheme.colorScheme.surfaceVariant
				)
			),
			onLongClick = { onLongClick(todoData) },
			onClick = {
				if (selection)
					onChangeSelect(todoData, !todoUiState.selected)
				else
					onClick(todoData)
			},
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
					allowGrouping = allowGrouping,
					onDismissRequest = { showDropdownMenu = false },
					onAddToGroup = { menuOnAddToGroup(todoData) },
					onRename = { menuOnRename(todoData) },
					onSelect = { menuOnSelect(todoData) },
					onDelete = { menuOnDelete(todoData) }
				)
			}
		}
	}
}

@Preview
@Composable
private fun PreviewSuperTodoCard() {
	var selection by remember { mutableStateOf(false) }
	var todoUiState by remember {
		mutableStateOf(
			TodoUiState(
				data = TodoData(id = 1, title = "Test", isDone = true)
			)
		)
	}
	SuperTodoCard(
		todoUiState = todoUiState,
		selection = selection,
		onChangeSelect = { _, selected ->
			todoUiState = todoUiState.copy(selected = selected)
		},
		onLongClick = { selection = !selection },
		onDoneChange = { _, isDone ->
			todoUiState = todoUiState.copy(data = todoUiState.data.copy(isDone = isDone))
		}
	)
}